package com.markettwits.selfupdater.thirdparty.api

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.File
import java.security.MessageDigest

sealed interface ApkFileVerification {
    data object Verified : ApkFileVerification
    data class Rejected(val reason: ApkVerificationFailure) : ApkFileVerification
    data class Unreadable(val cause: Exception? = null) : ApkFileVerification
}

/** Reads the actual file and Android package signatures; it never starts an installer. */
class AndroidApkVerifier(private val context: Context) {
    suspend fun verify(file: File, expected: ApkDownloadExpectation): ApkFileVerification = withContext(Dispatchers.IO) {
        try {
            val manager = context.packageManager
            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                PackageManager.GET_SIGNING_CERTIFICATES
            } else {
                @Suppress("DEPRECATION")
                PackageManager.GET_SIGNATURES
            }
            val installed = manager.installedPackage(context.packageName, flags)
            val archive = manager.archivePackage(file.absolutePath, flags) ?: return@withContext ApkFileVerification.Unreadable()
            if (!file.isFile) return@withContext ApkFileVerification.Unreadable()
            val failure = ApkTrustPolicy.verify(
                expected = expected,
                installed = installed.identity(),
                downloaded = archive.identity(),
                actualSizeBytes = file.length(),
                actualSha256 = file.sha256(),
            )
            if (failure != null) return@withContext ApkFileVerification.Rejected(failure)
            val applicationInfo = archive.applicationInfo ?: return@withContext ApkFileVerification.Unreadable()
            if (applicationInfo.minSdkVersion > Build.VERSION.SDK_INT) {
                return@withContext ApkFileVerification.Rejected(ApkVerificationFailure.UnsupportedAndroidVersion)
            }
            if (!apkSupportsDeviceAbis(file, Build.SUPPORTED_ABIS.toSet())) {
                return@withContext ApkFileVerification.Rejected(ApkVerificationFailure.UnsupportedArchitecture)
            }
            ApkFileVerification.Verified
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (failure: Exception) {
            ApkFileVerification.Unreadable(failure)
        }
    }
}

@Suppress("DEPRECATION")
private fun PackageManager.installedPackage(name: String, flags: Int): PackageInfo =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) getPackageInfo(name, PackageManager.PackageInfoFlags.of(flags.toLong()))
    else getPackageInfo(name, flags)

@Suppress("DEPRECATION")
private fun PackageManager.archivePackage(path: String, flags: Int): PackageInfo? =
    getPackageArchiveInfo(path, flags)

@Suppress("DEPRECATION")
private fun PackageInfo.identity(): ApkPackageIdentity {
    val signing = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val info = signingInfo
        ApkSigningIdentity(
            currentSigners = info?.apkContentsSigners?.map(Signature::fingerprint)?.toSet().orEmpty(),
            signingHistory = if (info?.hasMultipleSigners() == false) info.signingCertificateHistory?.map(Signature::fingerprint)?.toSet().orEmpty() else emptySet(),
            multipleSigners = info?.hasMultipleSigners() == true,
        )
    } else {
        val signers = signatures?.map(Signature::fingerprint)?.toSet().orEmpty()
        ApkSigningIdentity(currentSigners = signers, multipleSigners = signers.size > 1)
    }
    return ApkPackageIdentity(packageName.orEmpty(), buildNumber(), signing)
}

private fun Signature.fingerprint(): String = MessageDigest.getInstance("SHA-256").digest(toByteArray()).hex()

private suspend fun File.sha256(): String {
    val digest = MessageDigest.getInstance("SHA-256")
    inputStream().buffered().use { input ->
        val buffer = ByteArray(8192)
        while (true) {
            currentCoroutineContext().ensureActive()
            val count = input.read(buffer)
            if (count < 0) break
            digest.update(buffer, 0, count)
        }
    }
    return digest.digest().hex()
}

private fun ByteArray.hex(): String = joinToString("") { (it.toInt() and 0xff).toString(16).padStart(2, '0') }
