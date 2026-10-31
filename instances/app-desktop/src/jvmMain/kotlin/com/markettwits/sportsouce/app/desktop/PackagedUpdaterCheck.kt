package com.markettwits.sportsouce.app.desktop

import com.markettwits.selfupdater.api.SelfUpdaterApi
import com.markettwits.selfupdater.api.SelfUpdaterSourceApi
import com.markettwits.selfupdater.components.selft_update.di.selfUpdateComponentModule
import com.markettwits.selfupdater.thirdparty.api.DesktopArchitecture
import com.markettwits.selfupdater.thirdparty.api.DesktopDownloadRequest
import com.markettwits.selfupdater.thirdparty.api.DesktopPlatform
import com.markettwits.selfupdater.thirdparty.api.DesktopUpdateDownloader
import com.markettwits.selfupdater.thirdparty.api.SelfUpdaterDesktop
import com.markettwits.selfupdater.thirdparty.api.SparklePackagedCheck
import com.markettwits.selfupdater.thirdparty.github.model.DesktopReleaseManifest
import com.markettwits.selfupdater.thirdparty.github.model.GithubRelease
import com.markettwits.selfupdater.thirdparty.github.model.desktopAsset
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.koin.dsl.koinApplication
import java.nio.file.Files
import java.security.MessageDigest

/** Offline smoke check executed by CI through the actual packaged launcher and runtime. */
internal fun verifyPackagedUpdater() {
    val app = koinApplication { modules(selfUpdateComponentModule) }
    try {
        val source = app.koin.get<SelfUpdaterSourceApi>()
        check(source is SelfUpdaterDesktop && source.isSelfUpdateCanManualCheck())
        app.koin.get<SelfUpdaterApi>()
        val digest = "a".repeat(64)
        val name = "Sportsauce-1.6.1-10422.dmg"
        val url = "https://github.com/MarketTwits/Sportsouce/releases/download/1.6.1.10422/$name"
        val release = Json.decodeFromString<GithubRelease>("""{"tag_name":"1.6.1.10422","assets":[{"name":"$name","browser_download_url":"$url","state":"uploaded","size":4,"digest":"sha256:$digest"}]}""")
        val manifest = Json.decodeFromString<DesktopReleaseManifest>("""{"schemaVersion":1,"releaseId":"1.6.1.10422","versionName":"1.6.1","buildNumber":10422,"channel":"stable","assets":[{"platform":"macos","architecture":"arm64","packageType":"dmg","name":"$name","size":4,"sha256":"$digest"}]}""")
        check(release.desktopAsset(manifest, DesktopPlatform.MacOS, DesktopArchitecture.Arm64)?.name == name)
        val bytes = "test".toByteArray()
        val sha = MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { (it.toInt() and 255).toString(16).padStart(2, '0') }
        val directory = Files.createTempDirectory("sportsauce-updater-smoke-")
        try {
            runBlocking {
                SparklePackagedCheck.verify()
                System.getProperty("sportsauce.probe.library")?.let { library ->
                    SparklePackagedCheck.verifyNative(library, requireNotNull(System.getProperty("sportsauce.probe.expected")).toInt())
                }
                val downloader = DesktopUpdateDownloader(directory)
                val request = DesktopDownloadRequest(name, bytes.size.toLong(), sha)
                val downloaded = downloader.download(request, { bytes.inputStream() })
                check(downloader.findVerified(request) == downloaded)
            }
        } finally {
            directory.toFile().deleteRecursively()
        }
        println("SELF_UPDATER_SMOKE_OK")
    } finally {
        app.close()
    }
}
