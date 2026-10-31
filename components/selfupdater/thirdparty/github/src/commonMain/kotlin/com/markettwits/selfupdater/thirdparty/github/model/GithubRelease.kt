package com.markettwits.selfupdater.thirdparty.github.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.markettwits.selfupdater.models.ReleaseVersion

@Serializable
data class GithubRelease(
    @SerialName("tag_name")
    val tagName: String,
    @SerialName("assets")
    val assets: List<GithubAsset>,
    @SerialName("name")
    val name: String? = null,
    @SerialName("prerelease")
    val preRelease: Boolean = false,
    @SerialName("draft")
    val draft: Boolean = false,
    @SerialName("body")
    val description: String? = null
) {
    fun androidAsset(): GithubAsset {
        require(!draft && !preRelease) { "Release is not stable" }
        val version = requireNotNull(ReleaseVersion.fromTag(tagName)) { "Invalid release tag" }
        val expectedName = "Sportsauce-${version.versionName}-${version.buildNumber}.apk"
        val matches = assets.filter { it.name == expectedName && it.state == "uploaded" && it.size > 0 }
        require(matches.size == 1) { "Expected one uploaded Android APK" }
        val asset = matches.single()
        val expectedUrl = "https://github.com/MarketTwits/Sportsouce/releases/download/$tagName/$expectedName"
        require(asset.browserDownloadUrl == expectedUrl) { "Unexpected Android APK URL" }
        return asset
    }
}

@Serializable
data class GithubAsset(
    @SerialName("name")
    val name: String,
    @SerialName("browser_download_url")
    val browserDownloadUrl: String,
    @SerialName("state")
    val state: String,
    @SerialName("size")
    val size: Long,
    @SerialName("digest")
    val digest: String? = null,
) {
    fun sha256OrNull(): String? {
        val value = digest?.removePrefix("sha256:") ?: return null
        if (digest != "sha256:$value" || value.length != 64 || value.any { it !in '0'..'9' && it !in 'a'..'f' }) return null
        return value
    }
}
