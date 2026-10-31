package com.markettwits.selfupdater.thirdparty.api

/** Signing digests are SHA-256 fingerprints of Android signing certificates. */
data class ApkSigningIdentity(
    val currentSigners: Set<String>,
    val signingHistory: Set<String> = emptySet(),
    val multipleSigners: Boolean = false,
)

data class ApkPackageIdentity(
    val packageName: String,
    val buildNumber: Long,
    val signing: ApkSigningIdentity,
)

data class ApkDownloadExpectation(
    val buildNumber: Long,
    val sizeBytes: Long?,
    val sha256: String?,
)

enum class ApkVerificationFailure {
    InvalidMetadata,
    SizeMismatch,
    DigestMismatch,
    PackageMismatch,
    VersionMismatch,
    NotAnUpgrade,
    SigningMismatch,
    UnsupportedArchitecture,
    UnsupportedAndroidVersion,
}

/** Pure policy; the Android adapter obtains package identities and file digests from the OS. */
object ApkTrustPolicy {
    fun verify(
        expected: ApkDownloadExpectation,
        installed: ApkPackageIdentity,
        downloaded: ApkPackageIdentity,
        actualSizeBytes: Long,
        actualSha256: String,
    ): ApkVerificationFailure? {
        val digest = expected.sha256
        if (expected.buildNumber <= 0 || expected.sizeBytes == null || expected.sizeBytes <= 0 ||
            digest == null || !digest.isSha256()
        ) return ApkVerificationFailure.InvalidMetadata
        if (actualSizeBytes != expected.sizeBytes) return ApkVerificationFailure.SizeMismatch
        if (actualSha256 != digest) return ApkVerificationFailure.DigestMismatch
        if (installed.packageName.isBlank() || downloaded.packageName != installed.packageName) return ApkVerificationFailure.PackageMismatch
        if (downloaded.buildNumber != expected.buildNumber) return ApkVerificationFailure.VersionMismatch
        if (downloaded.buildNumber <= installed.buildNumber) return ApkVerificationFailure.NotAnUpgrade
        if (!signingMatches(installed.signing, downloaded.signing)) return ApkVerificationFailure.SigningMismatch
        return null
    }

    private fun signingMatches(installed: ApkSigningIdentity, downloaded: ApkSigningIdentity): Boolean {
        if (installed.currentSigners.isEmpty() || downloaded.currentSigners.isEmpty()) return false
        if (!(installed.currentSigners + downloaded.currentSigners + downloaded.signingHistory).all { it.isSha256() }) return false
        if (installed.multipleSigners || downloaded.multipleSigners) {
            return installed.multipleSigners && downloaded.multipleSigners && installed.currentSigners == downloaded.currentSigners
        }
        if (installed.currentSigners.size != 1 || downloaded.currentSigners.size != 1) return false
        if (installed.currentSigners == downloaded.currentSigners) return true
        return downloaded.signingHistory.containsAll(installed.currentSigners) &&
            downloaded.signingHistory.containsAll(downloaded.currentSigners)
    }

    private fun String.isSha256(): Boolean = length == 64 && all { it in '0'..'9' || it in 'a'..'f' }
}
