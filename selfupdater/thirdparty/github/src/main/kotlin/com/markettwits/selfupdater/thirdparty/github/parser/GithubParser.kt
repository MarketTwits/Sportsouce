package com.markettwits.selfupdater.thirdparty.github.parser

import android.util.Log
import com.markettwits.selfupdater.thirdparty.api.SelfUpdate
import com.markettwits.selfupdater.thirdparty.api.SelfUpdateParserApi
import com.markettwits.selfupdater.thirdparty.github.BuildConfig
import com.markettwits.selfupdater.thirdparty.github.model.GithubRelease
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

private const val GITHUB_API_LAST_RELEASE =
    "https://api.github.com/repos/MarketTwits/SportsouceMobile/releases/latest"

class GithubParser(
    private val client: HttpClient
) : SelfUpdateParserApi {

    override fun getName(): String = "Github. Google Feature: ${isGooglePlayEnable()}"

    override suspend fun getLastUpdate(): SelfUpdate? {
        val isDev = isDev()
        val update = parseReleaseUpdate()
        Log.e("mt05", "update : $update")

        val downloadUrl = update.getDownloadUrl(isDev = isDev()) ?: return null

        return SelfUpdate(
            version = update.getVersion(isDev),
            downloadUrl = downloadUrl,
            name = update.name,
            description = update.description
        )
    }

    private suspend fun parseReleaseUpdate(): GithubRelease {
        return client.get(
            urlString = GITHUB_API_LAST_RELEASE
        ).body()
    }


    private fun isGooglePlayEnable() = false

    private fun isDev(): Boolean = BuildConfig.DEBUG
}
