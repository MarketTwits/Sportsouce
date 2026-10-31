package com.markettwits.selfupdater.thirdparty.github

import com.markettwits.selfupdater.thirdparty.github.model.*
import com.markettwits.selfupdater.thirdparty.api.DesktopPlatform
import com.markettwits.selfupdater.thirdparty.api.DesktopArchitecture
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DesktopReleaseManifestTest {
    private val hash = "a".repeat(64)
    private val entry = DesktopManifestAsset("macos", "arm64", "dmg", "Sportsauce-1.6.1-10422.dmg", 123, hash)
    private val manifest = DesktopReleaseManifest(1, "1.6.1.10422", "1.6.1", 10422, "stable", listOf(entry))
    private val asset = GithubAsset(entry.name, "https://github.com/MarketTwits/Sportsouce/releases/download/1.6.1.10422/${entry.name}", "uploaded", 123, "sha256:$hash")
    private val release = GithubRelease("1.6.1.10422", listOf(asset))

    @Test
    fun selectsOnlyExplicitlyAdvertisedCompatibleArchitecture() {
        assertEquals(asset, release.desktopAsset(manifest, DesktopPlatform.MacOS, DesktopArchitecture.Arm64))
        assertNull(release.desktopAsset(manifest, DesktopPlatform.MacOS, DesktopArchitecture.X64))
        assertNull(release.desktopAsset(manifest, DesktopPlatform.Windows, DesktopArchitecture.Arm64))
    }

    @Test
    fun rejectsMismatchedVersionDuplicatedAssetsAndChangedFile() {
        val invalid = listOf(
            manifest.copy(schemaVersion = 2), manifest.copy(releaseId = "1.6.1.10423"),
            manifest.copy(versionName = "1.6.2"), manifest.copy(buildNumber = 10423),
            manifest.copy(channel = "prerelease"), manifest.copy(assets = listOf(entry, entry)),
            manifest.copy(assets = listOf(entry.copy(name = "../bad.dmg"))),
            manifest.copy(assets = listOf(entry.copy(size = 0))),
            manifest.copy(assets = listOf(entry.copy(sha256 = "b".repeat(64)))),
        )
        invalid.forEach { assertFailsWith<IllegalArgumentException> { release.desktopAsset(it, DesktopPlatform.MacOS, DesktopArchitecture.Arm64) } }
        assertFailsWith<IllegalArgumentException> { release.copy(assets = listOf(asset, asset)).desktopAsset(manifest, DesktopPlatform.MacOS, DesktopArchitecture.Arm64) }
        assertFailsWith<IllegalArgumentException> { release.copy(assets = listOf(asset.copy(browserDownloadUrl = "https://example.com/app.dmg"))).desktopAsset(manifest, DesktopPlatform.MacOS, DesktopArchitecture.Arm64) }
        assertFailsWith<IllegalArgumentException> { release.copy(preRelease = true).desktopAsset(manifest, DesktopPlatform.MacOS, DesktopArchitecture.Arm64) }
    }
}
