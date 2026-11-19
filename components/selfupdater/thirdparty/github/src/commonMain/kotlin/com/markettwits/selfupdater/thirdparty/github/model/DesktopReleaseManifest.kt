package com.markettwits.selfupdater.thirdparty.github.model

import com.markettwits.selfupdater.models.ReleaseVersion
import com.markettwits.selfupdater.thirdparty.api.DesktopPlatform
import com.markettwits.selfupdater.thirdparty.api.DesktopArchitecture
import kotlinx.serialization.Serializable

@Serializable
data class DesktopReleaseManifest(
    val schemaVersion: Int,
    val releaseId: String,
    val versionName: String,
    val buildNumber: Long,
    val channel: String,
    val assets: List<DesktopManifestAsset>,
)

@Serializable
data class DesktopManifestAsset(
    val platform: String,
    val architecture: String,
    val packageType: String,
    val name: String,
    val size: Long,
    val sha256: String,
)

/** Returns no candidate when a release does not advertise a unique compatible binary. */
fun GithubRelease.desktopAsset(
    manifest: DesktopReleaseManifest,
    platform: DesktopPlatform,
    architecture: DesktopArchitecture,
): GithubAsset? {
    require(!draft && !preRelease) { "Release is not stable" }
    val version = requireNotNull(ReleaseVersion.fromTag(tagName)) { "Invalid release tag" }
    require(manifest.schemaVersion == 1 && manifest.releaseId == tagName && manifest.channel == "stable" &&
        manifest.versionName == version.versionName && manifest.buildNumber == version.buildNumber) { "Invalid release manifest" }
    val os = when (platform) { DesktopPlatform.Windows -> "windows"; DesktopPlatform.MacOS -> "macos" }
    val cpu = when (architecture) { DesktopArchitecture.X64 -> "x64"; DesktopArchitecture.Arm64 -> "arm64" }
    val extension = when (platform) { DesktopPlatform.Windows -> "exe"; DesktopPlatform.MacOS -> "dmg" }
    val expectedName = "Sportsauce-${version.versionName}-${version.buildNumber}.$extension"
    val candidates = manifest.assets.filter { it.platform == os && it.architecture == cpu && it.packageType == extension }
    if (candidates.isEmpty()) return null
    require(candidates.size == 1) { "Ambiguous desktop asset" }
    val entry = candidates.single()
    require(entry.name == expectedName && entry.size > 0 && entry.sha256.length == 64 &&
        entry.sha256.all { it in '0'..'9' || it in 'a'..'f' }) { "Invalid desktop asset metadata" }
    val uploaded = assets.filter { it.name == entry.name && it.state == "uploaded" }
    require(uploaded.size == 1) { "Desktop asset is unavailable or ambiguous" }
    val asset = uploaded.single()
    require(asset.size == entry.size && asset.sha256OrNull() == entry.sha256 &&
        asset.browserDownloadUrl == "https://github.com/MarketTwits/Sportsouce/releases/download/$tagName/$expectedName") {
        "Desktop asset does not match release metadata"
    }
    return asset
}
