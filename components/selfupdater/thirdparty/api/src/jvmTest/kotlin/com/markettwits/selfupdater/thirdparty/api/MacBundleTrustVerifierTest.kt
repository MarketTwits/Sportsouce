package com.markettwits.selfupdater.thirdparty.api

import kotlinx.coroutines.test.runTest
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import java.nio.file.StandardCopyOption

class MacBundleTrustVerifierTest {
    @Test
    fun optInRealDeveloperIdBundlePassesSystemVerification() = runTest {
        val bundle = System.getenv("SPORTSAUCE_TEST_SIGNED_MAC_BUNDLE") ?: return@runTest
        val bundleId = requireNotNull(System.getenv("SPORTSAUCE_TEST_SIGNED_MAC_BUNDLE_ID"))
        val teamId = requireNotNull(System.getenv("SPORTSAUCE_TEST_SIGNED_MAC_TEAM_ID"))
        assertTrue(MacBundleTrustVerifier().verify(java.nio.file.Path.of(bundle), bundleId, teamId))
        println("Verified real Developer ID test bundle")
    }

    @Test
    fun realAdHocSignedMacBundleCannotPassPublisherCheck() = runTest {
        if (!System.getProperty("os.name").startsWith("Mac", ignoreCase = true)) return@runTest
        val parent = Files.createTempDirectory("sportsauce-adhoc-test")
        try {
            val bundle = Files.createDirectories(parent.resolve("SportSauce.app/Contents/MacOS")).parent.parent
            Files.copy(java.nio.file.Path.of("/usr/bin/true"), bundle.resolve("Contents/MacOS/test"), StandardCopyOption.REPLACE_EXISTING)
            Files.writeString(bundle.resolve("Contents/Info.plist"), """<?xml version="1.0" encoding="UTF-8"?>
                <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
                <plist version="1.0"><dict><key>CFBundleIdentifier</key><string>com.markettwits.sportsouce.desktop</string>
                <key>CFBundleExecutable</key><string>test</string><key>CFBundlePackageType</key><string>APPL</string></dict></plist>""")
            val sign = ProcessBuilder("/usr/bin/codesign", "--force", "--sign", "-", bundle.toString())
                .redirectErrorStream(true).start()
            val output = sign.inputStream.bufferedReader().readText()
            assertTrue(sign.waitFor() == 0, output)
            assertFalse(MacBundleTrustVerifier().verify(bundle, "com.markettwits.sportsouce.desktop", "ABCDE12345"))
        } finally { parent.toFile().deleteRecursively() }
    }
    @Test
    fun requiresValidSignaturePinnedIdentityAndGatekeeperAcceptance() = runTest {
        val parent = Files.createTempDirectory("sportsauce-trust-test")
        val bundle = Files.createDirectory(parent.resolve("SportSauce.app"))
        try {
            var verification = CommandResult(0, "")
            var details = CommandResult(0, "Identifier=com.markettwits.sportsouce.desktop\nTeamIdentifier=ABCDE12345\nSignature size=1234\n")
            var gatekeeper = CommandResult(0, "accepted")
            val commands = mutableListOf<List<String>>()
            val verifier = MacBundleTrustVerifier { command ->
                commands += command
                when (command[1]) {
                    "--verify" -> verification
                    "--display" -> details
                    else -> gatekeeper
                }
            }
            suspend fun verify() = verifier.verify(bundle, "com.markettwits.sportsouce.desktop", "ABCDE12345")
            assertTrue(verify())
            assertTrue(commands.all { it.last() == bundle.toString() })
            gatekeeper = CommandResult(1, "rejected")
            assertFalse(verify())
            gatekeeper = CommandResult(0, "accepted")
            details = details.copy(output = details.output.replace("ABCDE12345", "OTHER12345"))
            assertFalse(verify())
            details = details.copy(output = "Identifier=com.markettwits.sportsouce.desktop\nTeamIdentifier=ABCDE12345\nSignature=adhoc\n")
            assertFalse(verify())
            details = details.copy(output = "Identifier=com.markettwits.sportsouce.desktop\nTeamIdentifier=ABCDE12345\n")
            assertFalse(verify())
            details = details.copy(output = "Identifier=com.markettwits.sportsouce.desktop\nTeamIdentifier=ABCDE12345\nSignature size=1234\nSignature size=1234\n")
            assertFalse(verify())
            details = details.copy(output = "Identifier=com.markettwits.sportsouce.desktop\nTeamIdentifier=ABCDE12345\nSignature size=0\n")
            assertFalse(verify())
            details = details.copy(output = "Identifier=com.markettwits.sportsouce.desktop\nTeamIdentifier=ABCDE12345\nSignature size=1234\nSignature=adhoc\nSignature=adhoc\n")
            assertFalse(verify())
            details = details.copy(output = "Identifier=com.markettwits.sportsouce.desktop\nTeamIdentifier=ABCDE12345\nTeamIdentifier=OTHER12345\n")
            assertFalse(verify())
            verification = CommandResult(1, "invalid")
            assertFalse(verify())
            assertFalse(verifier.verify(bundle, "com.markettwits.sportsouce.desktop", ""))
        } finally { parent.toFile().deleteRecursively() }
    }

    @Test
    fun rejectsSymlinkToBundleBeforeRunningAnyCommand() = runTest {
        val parent = Files.createTempDirectory("sportsauce-trust-test")
        try {
            val bundle = Files.createDirectory(parent.resolve("Real.app"))
            val link = parent.resolve("Link.app")
            try { Files.createSymbolicLink(link, bundle) } catch (_: Exception) { return@runTest }
            val verifier = MacBundleTrustVerifier { error("Must not inspect a symlink") }
            assertFalse(verifier.verify(link, "com.markettwits.sportsouce.desktop", "ABCDE12345"))
        } finally { parent.toFile().deleteRecursively() }
    }
}
