package com.markettwits.selfupdater.thirdparty.github

import com.markettwits.core_cloud.provider.defaultHttpClientEngine
import com.markettwits.selfupdater.thirdparty.api.DesktopArchitecture
import com.markettwits.selfupdater.thirdparty.api.DesktopDownloadRequest
import com.markettwits.selfupdater.thirdparty.api.DesktopPlatform
import com.markettwits.selfupdater.thirdparty.api.DesktopUpdateDownloader
import com.markettwits.selfupdater.thirdparty.api.GithubDesktopDownloadSource
import com.markettwits.selfupdater.thirdparty.github.di.configureGithubClient
import com.markettwits.selfupdater.thirdparty.github.parser.GithubParser
import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking
import org.junit.Assume.assumeTrue
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/** Opt-in network test; never opens or installs the downloaded artifact. */
class GithubDesktopLiveTest {
    @Test
    fun actualLegacyDesktopAssetsCanBeResolvedDownloadedAndVerified() = runBlocking {
        assumeTrue("Set SPORTSAUCE_LIVE_UPDATE_TEST=1 to download the real GitHub artifact", System.getenv("SPORTSAUCE_LIVE_UPDATE_TEST") == "1")
        val client = HttpClient(defaultHttpClientEngine()) { configureGithubClient() }
        val directory = Files.createTempDirectory("sportsauce-live-update-")
        try {
            val parser = GithubParser(client) { "en" }
            val downloader = DesktopUpdateDownloader(directory)
            val artifacts = listOf(
                Triple(DesktopPlatform.MacOS, DesktopArchitecture.Arm64, "Sportsauce-1.6.1-10422.dmg"),
                Triple(DesktopPlatform.Windows, DesktopArchitecture.X64, "Sportsauce-1.6.1-10422.exe"),
            )
            for ((platform, architecture, name) in artifacts) {
                val update = assertNotNull(parser.getLatestDesktopUpdate(platform, architecture))
                assertEquals("1.6.1.10422", update.version, "This test fixture must be revised when latest release changes")
                assertEquals(name, update.name)
                val request = DesktopDownloadRequest(update.name, assertNotNull(update.sizeBytes), assertNotNull(update.sha256))
                var last = 0L
                val path = downloader.download(request, GithubDesktopDownloadSource(update.downloadUrl)) { bytes, _ -> last = bytes }
                assertEquals(request.sizeBytes, last)
                assertEquals(request.sizeBytes, Files.size(path))
                assertEquals(path, downloader.findVerified(request))
                assertTrue(path.startsWith(directory))
                Files.delete(path)
            }
        } finally {
            client.close()
            directory.toFile().deleteRecursively()
        }
    }
}
