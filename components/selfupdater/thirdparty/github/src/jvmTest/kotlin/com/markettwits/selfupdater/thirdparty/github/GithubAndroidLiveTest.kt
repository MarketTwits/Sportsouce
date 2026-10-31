package com.markettwits.selfupdater.thirdparty.github

import com.markettwits.core_cloud.provider.defaultHttpClientEngine
import com.markettwits.selfupdater.thirdparty.api.androidDownloadMetadata
import com.markettwits.selfupdater.thirdparty.github.di.configureGithubClient
import com.markettwits.selfupdater.thirdparty.github.parser.GithubParser
import io.ktor.client.HttpClient
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import kotlinx.coroutines.runBlocking
import org.junit.Assume.assumeTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/** Opt-in check of real GitHub metadata against a separately downloaded APK; never installs it. */
class GithubAndroidLiveTest {
    @Test
    fun publishedApkMatchesWhatTheUpdaterSelects() = runBlocking {
        val file = System.getenv("SPORTSAUCE_LIVE_ANDROID_APK")
        assumeTrue("Set SPORTSAUCE_LIVE_ANDROID_APK to a downloaded latest-release APK", !file.isNullOrBlank())
        val path = Path.of(requireNotNull(file))
        val client = HttpClient(defaultHttpClientEngine()) { configureGithubClient() }
        try {
            val release = assertNotNull(GithubParser(client) { "ru" }.getLastUpdate())
            val metadata = assertNotNull(release.androidDownloadMetadata())
            assertEquals(metadata.sizeBytes, Files.size(path))
            val digest = MessageDigest.getInstance("SHA-256")
            Files.newInputStream(path).buffered().use { input ->
                val buffer = ByteArray(8192)
                while (true) {
                    val count = input.read(buffer)
                    if (count < 0) break
                    digest.update(buffer, 0, count)
                }
            }
            assertEquals(metadata.sha256, digest.digest().joinToString("") { (it.toInt() and 0xff).toString(16).padStart(2, '0') })
        } finally { client.close() }
    }
}
