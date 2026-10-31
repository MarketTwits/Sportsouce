package com.markettwits.selfupdater.thirdparty.github.model

import com.markettwits.selfupdater.thirdparty.api.DesktopArchitecture
import com.markettwits.selfupdater.thirdparty.api.DesktopPlatform

/**
 * Compatibility for the inspected release which predates update-manifest.json.
 * The hashes and architectures identify exact published artifacts, not future releases.
 */
internal fun GithubRelease.legacyDesktopAsset(platform: DesktopPlatform, architecture: DesktopArchitecture): GithubAsset? {
    if (draft || preRelease || tagName != "1.6.1.10422") return null
    val spec = when (platform to architecture) {
        DesktopPlatform.MacOS to DesktopArchitecture.Arm64 -> LegacyAsset(
            "Sportsauce-1.6.1-10422.dmg", 80_903_862,
            "718a5a287933f627e11ffb8a421692ac4dfd498ffbc7477ed0a5377f9114a49f",
        )
        DesktopPlatform.Windows to DesktopArchitecture.X64 -> LegacyAsset(
            "Sportsauce-1.6.1-10422.exe", 89_354_752,
            "413cbfc6ad4a9d890699e542a01850661640db06029b1bdd50a5b4faed4c4368",
        )
        else -> return null
    }
    val matching = assets.filter { it.name == spec.name }
    val asset = matching.singleOrNull() ?: return null
    if (asset.state != "uploaded" || asset.size != spec.size || asset.sha256OrNull() != spec.sha256 ||
        asset.browserDownloadUrl != "https://github.com/MarketTwits/Sportsouce/releases/download/$tagName/${spec.name}"
    ) return null
    return asset
}

private data class LegacyAsset(val name: String, val size: Long, val sha256: String)
