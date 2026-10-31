package com.markettwits.selfupdater.thirdparty.api

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import java.nio.file.Files
import java.net.URI
import java.security.MessageDigest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DesktopUpdateDownloaderTest {
    private val bytes = ByteArray(200_000) { (it % 251).toByte() }
    private val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
        .joinToString("") { (it.toInt() and 255).toString(16).padStart(2, '0') }
    private val request = DesktopDownloadRequest("Sportsauce-1.6.1-10422.exe", bytes.size.toLong(), digest)

    @Test
    fun insufficientSpaceDoesNotOpenNetworkOrReplaceExistingInstaller() = runTest {
        val dir = Files.createTempDirectory("sportsauce-downloader-space")
        try {
            val target = dir.resolve(request.fileName)
            Files.write(target, byteArrayOf(42))
            var networkOpened = false
            val downloader = DesktopUpdateDownloader(dir) { request.sizeBytes - 1 }
            assertFailsWith<IllegalStateException> {
                downloader.download(request, { networkOpened = true; bytes.inputStream() })
            }
            assertFalse(networkOpened)
            assertContentEquals(byteArrayOf(42), Files.readAllBytes(target))
            assertEquals(1L, Files.list(dir).use { it.count() })
        } finally { dir.toFile().deleteRecursively() }
    }

    @Test
    fun interruptedStreamDeletesPartialAndPreservesExistingInstaller() = runTest {
        val dir = Files.createTempDirectory("sportsauce-downloader-io")
        try {
            val target = dir.resolve(request.fileName)
            Files.write(target, byteArrayOf(42))
            val source = object : java.io.InputStream() {
                var count = 0
                override fun read(): Int {
                    if (count++ > 100) throw java.io.IOException("storage or network interruption")
                    return 1
                }
            }
            assertFailsWith<java.io.IOException> { DesktopUpdateDownloader(dir).download(request, { source }) }
            assertContentEquals(byteArrayOf(42), Files.readAllBytes(target))
            assertEquals(1L, Files.list(dir).use { it.count() })
        } finally { dir.toFile().deleteRecursively() }
    }

    @Test
    fun onlyHttpsGithubReleaseAndKnownCdnRedirectsAreAllowed() {
        val asset = "https://github.com/MarketTwits/Sportsouce/releases/download/1.6.1.10422/Sportsauce-1.6.1-10422.exe"
        assertTrue(isAllowedReleaseUrl(URI(asset)))
        assertFalse(isAllowedReleaseUrl(URI(asset.replace("https://", "http://"))))
        assertFalse(isAllowedReleaseUrl(URI(asset.replace("github.com", "github.com.evil.example"))))
        assertFalse(isAllowedReleaseUrl(URI(asset + "?redirect=other")))
        assertFalse(isAllowedReleaseUrl(URI(asset.replace(".exe", ".apk"))))
        assertFalse(isAllowedReleaseUrl(URI(asset.replace("-10422.exe", "-10423.exe"))))
        assertTrue(isAllowedDownloadUrl(URI("https://release-assets.githubusercontent.com/path?signature=abc")))
        assertFalse(isAllowedDownloadUrl(URI("https://release-assets.githubusercontent.com.evil.example/path")))
        assertFalse(isAllowedDownloadUrl(URI("http://release-assets.githubusercontent.com/path")))
        assertFalse(isAllowedDownloadUrl(URI("https://user@github.com/path")))
    }

    @Test
    fun successfulDownloadReportsProgressAndPublishesVerifiedFile() = runTest {
        val dir = Files.createTempDirectory("sportsauce-downloader-test")
        try {
            val progress = mutableListOf<Long>()
            val file = DesktopUpdateDownloader(dir).download(request, { bytes.inputStream() }) { read, _ -> progress += read }
            assertContentEquals(bytes, Files.readAllBytes(file))
            assertEquals(bytes.size.toLong(), progress.last())
            assertTrue(progress.zipWithNext().all { (a, b) -> b > a })
            assertEquals(listOf(request.fileName), Files.list(dir).use { stream -> stream.map { it.fileName.toString() }.toList() })
        } finally { dir.toFile().deleteRecursively() }
    }

    @Test
    fun restoresOnlyFilesMatchingFreshMetadataAndRejectsSymlinks() = runTest {
        val dir = Files.createTempDirectory("sportsauce-downloader-restore")
        try {
            val downloader = DesktopUpdateDownloader(dir)
            assertEquals(null, downloader.findVerified(request))
            val file = downloader.download(request, { bytes.inputStream() })
            assertEquals(file, downloader.findVerified(request))
            assertEquals(null, downloader.findVerified(request.copy(sha256 = "0".repeat(64))))
            Files.write(file, bytes.copyOf(bytes.size - 1))
            assertEquals(null, downloader.findVerified(request))
            if (!System.getProperty("os.name").startsWith("Windows", ignoreCase = true)) {
                Files.delete(file)
                val other = dir.resolve("other")
                Files.write(other, bytes)
                Files.createSymbolicLink(file, other)
                assertEquals(null, downloader.findVerified(request))
            }
            assertFailsWith<IllegalArgumentException> { downloader.findVerified(request.copy(fileName = "../other")) }
        } finally { dir.toFile().deleteRecursively() }
    }

    @Test
    fun rejectsInvalidMetadataTruncationAndCorruptionWithoutReplacingExistingFile() = runTest {
        val dir = Files.createTempDirectory("sportsauce-downloader-test")
        try {
            val target = dir.resolve(request.fileName)
            Files.write(target, byteArrayOf(42))
            val downloader = DesktopUpdateDownloader(dir)
            assertFailsWith<IllegalArgumentException> { downloader.download(request.copy(fileName = "../app.exe"), { bytes.inputStream() }) }
            assertFailsWith<IllegalArgumentException> { downloader.download(request.copy(sha256 = "bad"), { bytes.inputStream() }) }
            assertFailsWith<IllegalStateException> { downloader.download(request, { bytes.copyOf(2).inputStream() }) }
            assertFailsWith<IllegalStateException> { downloader.download(request, { (bytes + 1).inputStream() }) }
            assertFailsWith<IllegalStateException> { downloader.download(request.copy(sha256 = "0".repeat(64)), { bytes.inputStream() }) }
            assertFailsWith<java.io.IOException> { downloader.download(request, { throw java.io.IOException("offline") }) }
            assertContentEquals(byteArrayOf(42), Files.readAllBytes(target))
            assertEquals(1L, Files.list(dir).use { it.count() })
        } finally { dir.toFile().deleteRecursively() }
    }

    @Test
    fun cancellationDeletesPartialFile() = runTest {
        val dir = Files.createTempDirectory("sportsauce-downloader-test")
        try {
            val started = kotlinx.coroutines.CompletableDeferred<Unit>()
            val job = launch {
                DesktopUpdateDownloader(dir).download(request, { bytes.inputStream() }) { _, _ ->
                    started.complete(Unit)
                    throw CancellationException("cancelled")
                }
            }
            started.await()
            job.cancelAndJoin()
            assertFalse(Files.exists(dir.resolve(request.fileName)))
            assertEquals(0L, Files.list(dir).use { it.count() })
        } finally { dir.toFile().deleteRecursively() }
    }
}
