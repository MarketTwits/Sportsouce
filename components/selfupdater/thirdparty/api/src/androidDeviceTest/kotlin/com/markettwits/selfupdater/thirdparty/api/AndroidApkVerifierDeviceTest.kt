package com.markettwits.selfupdater.thirdparty.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.core.content.FileProvider
import android.content.pm.PackageManager
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.security.MessageDigest
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.test.assertNotNull

/** Uses the isolated instrumentation target APK as a real OS-parsed signed archive. */
@RunWith(AndroidJUnit4::class)
class AndroidApkVerifierDeviceTest {
    @Test
    fun realInstalledArchiveIsParsedAndCannotMasqueradeAsANewBuild() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val installed = context.getPackageInfo()
        val build = installed.buildNumber()
        val directory = File(context.filesDir, "self-updater").apply { mkdirs() }
        val archive = File(directory, "instrumentation-fixture.apk")
        try {
            File(context.applicationInfo.sourceDir).copyTo(archive, overwrite = true)
            assertTrue(apkSupportsDeviceAbis(archive, android.os.Build.SUPPORTED_ABIS.toSet()))
            val digest = MessageDigest.getInstance("SHA-256")
            archive.inputStream().buffered().use { input ->
                val buffer = ByteArray(8192)
                while (true) {
                    val count = input.read(buffer)
                    if (count < 0) break
                    digest.update(buffer, 0, count)
                }
            }
            val sha = digest.digest().joinToString("") { (it.toInt() and 255).toString(16).padStart(2, '0') }
            val verifier = AndroidApkVerifier(context)
            val expected = ApkDownloadExpectation(build + 1, archive.length(), sha)
            assertEquals(ApkVerificationFailure.VersionMismatch, assertIs<ApkFileVerification.Rejected>(verifier.verify(archive, expected)).reason)
            assertEquals(ApkVerificationFailure.DigestMismatch, assertIs<ApkFileVerification.Rejected>(verifier.verify(archive, expected.copy(sha256 = "0".repeat(64)))).reason)
            if (android.os.Build.VERSION.SDK_INT >= 28) {
                @Suppress("DEPRECATION")
                val parsed = context.packageManager.getPackageArchiveInfo(archive.absolutePath, PackageManager.GET_SIGNING_CERTIFICATES)
                @Suppress("DEPRECATION")
                val current = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNING_CERTIFICATES)
                assertEquals(context.packageName, parsed?.packageName)
                val signers = assertNotNull(current.signingInfo?.apkContentsSigners).toList()
                assertTrue(signers.isNotEmpty())
                assertEquals(signers, parsed?.signingInfo?.apkContentsSigners?.toList())
            }
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.selfupdater.files", archive)
            assertEquals("content", uri.scheme)
            assertTrue(context.contentResolver.openInputStream(uri)?.use { it.read() } != null)
        } finally {
            archive.delete()
        }
    }
}
