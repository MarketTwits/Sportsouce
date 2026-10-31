package com.markettwits.selfupdater.thirdparty.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path
import java.util.concurrent.TimeUnit

/** A successful digest only proves integrity; automatic replacement also requires publisher trust. */
internal class MacBundleTrustVerifier(
    private val run: suspend (List<String>) -> CommandResult = ::runMacVerificationCommand,
) {
    suspend fun verify(bundle: Path, expectedBundleId: String, expectedTeamId: String): Boolean {
        if (!expectedBundleId.matches(Regex("[A-Za-z0-9-]+(?:\\.[A-Za-z0-9-]+)+")) ||
            !expectedTeamId.matches(Regex("[A-Z0-9]{10}")) ||
            !Files.isDirectory(bundle, LinkOption.NOFOLLOW_LINKS) || !bundle.fileName.toString().endsWith(".app")
        ) return false
        val path = bundle.toAbsolutePath().normalize().toString()
        val validation = run(listOf("/usr/bin/codesign", "--verify", "--deep", "--strict", "--verbose=2", path))
        if (validation.exitCode != 0) return false
        val details = run(listOf("/usr/bin/codesign", "--display", "--verbose=4", path))
        if (details.exitCode != 0) return false
        fun uniqueValue(key: String): String? = details.output.lineSequence()
            .filter { it.startsWith("$key=") }.map { it.substringAfter('=') }.toList().singleOrNull()
        // codesign prints "Signature=adhoc" for ad-hoc signatures and
        // "Signature size=<bytes>" for a CMS signature.
        val signatureSize = uniqueValue("Signature size")?.toLongOrNull()
        if (uniqueValue("Identifier") != expectedBundleId || uniqueValue("TeamIdentifier") != expectedTeamId ||
            signatureSize == null || signatureSize <= 0 || details.output.lineSequence().any { it.startsWith("Signature=") }
        ) return false
        // Gatekeeper must accept the package as well as matching the pinned developer team.
        return run(listOf("/usr/sbin/spctl", "--assess", "--type", "execute", path)).exitCode == 0
    }
}

internal data class CommandResult(val exitCode: Int, val output: String)

private suspend fun runMacVerificationCommand(command: List<String>): CommandResult = withContext(Dispatchers.IO) {
    val output = Files.createTempFile("sportsauce-codesign-", ".log")
    try {
        val process = ProcessBuilder(command).redirectErrorStream(true).redirectOutput(output.toFile()).start()
        try {
            if (!process.waitFor(30, TimeUnit.SECONDS)) {
                process.destroyForcibly()
                return@withContext CommandResult(-1, "")
            }
            val text = Files.newInputStream(output).use { it.readNBytes(65_537) }
            if (text.size > 65_536) return@withContext CommandResult(-1, "")
            CommandResult(process.exitValue(), text.toString(Charsets.UTF_8))
        } finally {
            if (process.isAlive) process.destroyForcibly()
        }
    } finally {
        Files.deleteIfExists(output)
    }
}
