package com.markettwits.selfupdater.thirdparty.github.model

import android.util.Log
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


private const val RELEASE_SUFFIX = "-release"
private const val DEBUG_SUFFIX = "-debug"
private const val APK_EXTENSION = ".apk"

@Serializable
data class GithubRelease(
    @SerialName("tag_name")
    val tagName: String,
    @SerialName("assets")
    val assets: List<GithubAsset>,
    @SerialName("name")
    val name: String,
    @SerialName("prerelease")
    val preRelease: Boolean,
    @SerialName("body")
    val description: String
) {
    fun getDownloadUrl(isDev: Boolean): String? {
        val suffix = if (isDev) DEBUG_SUFFIX else RELEASE_SUFFIX
        val matchingAssets = assets.filter { it.name.contains(suffix) }
        Log.e("mt05", "matchingAssets $matchingAssets")
        return matchingAssets.firstOrNull()?.browserDownloadUrl
    }

    fun getVersion(isDev: Boolean): String {
        return if (isDev) {
            tagName.removeSuffix(DEBUG_SUFFIX)
        } else {
            tagName.removeSuffix(RELEASE_SUFFIX)
        }
    }
}

@Serializable
data class GithubAsset(
    @SerialName("name")
    val name: String,
    @SerialName("browser_download_url")
    val browserDownloadUrl: String
)
