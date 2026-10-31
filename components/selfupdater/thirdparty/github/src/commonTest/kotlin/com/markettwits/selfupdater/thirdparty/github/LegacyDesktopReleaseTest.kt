package com.markettwits.selfupdater.thirdparty.github

import com.markettwits.selfupdater.thirdparty.api.DesktopArchitecture
import com.markettwits.selfupdater.thirdparty.api.DesktopPlatform
import com.markettwits.selfupdater.thirdparty.github.model.GithubAsset
import com.markettwits.selfupdater.thirdparty.github.model.GithubRelease
import com.markettwits.selfupdater.thirdparty.github.model.legacyDesktopAsset
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LegacyDesktopReleaseTest {
    private val tag = "1.6.1.10422"
    private val mac = GithubAsset("Sportsauce-1.6.1-10422.dmg", "https://github.com/MarketTwits/Sportsouce/releases/download/$tag/Sportsauce-1.6.1-10422.dmg", "uploaded", 80_903_862, "sha256:718a5a287933f627e11ffb8a421692ac4dfd498ffbc7477ed0a5377f9114a49f")
    private val windows = GithubAsset("Sportsauce-1.6.1-10422.exe", "https://github.com/MarketTwits/Sportsouce/releases/download/$tag/Sportsauce-1.6.1-10422.exe", "uploaded", 89_354_752, "sha256:413cbfc6ad4a9d890699e542a01850661640db06029b1bdd50a5b4faed4c4368")

    @Test
    fun selectsOnlyInspectedArchitectureAndExactArtifact() {
        val release = GithubRelease(tag, listOf(mac, windows))
        assertEquals(mac, release.legacyDesktopAsset(DesktopPlatform.MacOS, DesktopArchitecture.Arm64))
        assertEquals(windows, release.legacyDesktopAsset(DesktopPlatform.Windows, DesktopArchitecture.X64))
        assertNull(release.legacyDesktopAsset(DesktopPlatform.MacOS, DesktopArchitecture.X64))
        assertNull(release.legacyDesktopAsset(DesktopPlatform.Windows, DesktopArchitecture.Arm64))
        assertNull(release.copy(tagName = "1.6.1.10423").legacyDesktopAsset(DesktopPlatform.MacOS, DesktopArchitecture.Arm64))
        assertNull(release.copy(draft = true).legacyDesktopAsset(DesktopPlatform.MacOS, DesktopArchitecture.Arm64))
        for (bad in listOf(mac.copy(size = mac.size + 1), mac.copy(digest = null), mac.copy(digest = "sha256:${"0".repeat(64)}"), mac.copy(state = "new"), mac.copy(browserDownloadUrl = "https://example.com/file"))) {
            assertNull(release.copy(assets = listOf(bad)).legacyDesktopAsset(DesktopPlatform.MacOS, DesktopArchitecture.Arm64))
        }
        assertNull(release.copy(assets = listOf(mac, mac)).legacyDesktopAsset(DesktopPlatform.MacOS, DesktopArchitecture.Arm64))
    }
}
