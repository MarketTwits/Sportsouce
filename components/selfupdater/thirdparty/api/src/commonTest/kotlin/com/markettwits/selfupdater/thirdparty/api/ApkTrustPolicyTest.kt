package com.markettwits.selfupdater.thirdparty.api

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ApkTrustPolicyTest {
    private val firstSigner = "a".repeat(64)
    private val secondSigner = "b".repeat(64)
    private val digest = "c".repeat(64)
    private val installed = ApkPackageIdentity("com.markettwits.sportsouce", 10421, ApkSigningIdentity(setOf(firstSigner)))
    private val next = installed.copy(buildNumber = 10422)
    private val expected = ApkDownloadExpectation(10422, 1024, digest)

    private fun verify(
        expected: ApkDownloadExpectation = this.expected,
        installed: ApkPackageIdentity = this.installed,
        downloaded: ApkPackageIdentity = next,
        size: Long = 1024,
        actualDigest: String = digest,
    ) = ApkTrustPolicy.verify(expected, installed, downloaded, size, actualDigest)

    @Test
    fun acceptsOnlyMatchingReleaseForInstalledPackage() {
        assertNull(verify())
        assertEquals(ApkVerificationFailure.InvalidMetadata, verify(expected = expected.copy(sha256 = null)))
        assertEquals(ApkVerificationFailure.InvalidMetadata, verify(expected = expected.copy(sizeBytes = 0)))
        assertEquals(ApkVerificationFailure.SizeMismatch, verify(size = 1023))
        assertEquals(ApkVerificationFailure.DigestMismatch, verify(actualDigest = "d".repeat(64)))
        assertEquals(ApkVerificationFailure.PackageMismatch, verify(downloaded = next.copy(packageName = "other.app")))
        assertEquals(ApkVerificationFailure.VersionMismatch, verify(downloaded = next.copy(buildNumber = 10423)))
        assertEquals(ApkVerificationFailure.NotAnUpgrade, verify(installed = installed.copy(buildNumber = 10422)))
    }

    @Test
    fun acceptsVerifiedSingleSignerRotationAndRejectsUnknownSignatures() {
        val rotated = next.copy(signing = ApkSigningIdentity(setOf(secondSigner), setOf(firstSigner, secondSigner)))
        assertNull(verify(downloaded = rotated))
        assertEquals(ApkVerificationFailure.SigningMismatch, verify(downloaded = rotated.copy(signing = ApkSigningIdentity(setOf(secondSigner)))))
        assertEquals(ApkVerificationFailure.SigningMismatch, verify(downloaded = next.copy(signing = ApkSigningIdentity(emptySet()))))
        assertEquals(ApkVerificationFailure.SigningMismatch, verify(downloaded = rotated.copy(signing = ApkSigningIdentity(setOf(secondSigner), setOf(firstSigner)))))
    }

    @Test
    fun multiSignerPackagesRequireExactlyTheSameSigners() {
        val multi = ApkSigningIdentity(setOf(firstSigner, secondSigner), multipleSigners = true)
        assertNull(verify(installed = installed.copy(signing = multi), downloaded = next.copy(signing = multi)))
        assertEquals(ApkVerificationFailure.SigningMismatch, verify(installed = installed.copy(signing = multi)))
        assertEquals(ApkVerificationFailure.SigningMismatch, verify(downloaded = next.copy(signing = multi)))
    }
}
