package com.markettwits.selfupdater.thirdparty.github.parser

import android.util.Log
import com.markettwits.selfupdater.thirdparty.api.SelfUpdate
import com.markettwits.selfupdater.thirdparty.api.SelfUpdateParserApi
import com.markettwits.selfupdater.thirdparty.github.model.GithubAsset
import com.markettwits.selfupdater.thirdparty.github.model.GithubRelease
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

private const val GITHUB_API_ALL_RELEASES =
    "https://api.github.com/repos/MarketTwits/SportsouceMobile/releases"
private const val GITHUB_API_LAST_RELEASE =
    "https://api.github.com/repos/MarketTwits/SportsouceMobile/releases/latest"

private const val DEV_BUILD_TYPE = "internal"

class GithubParser(
    private val client: HttpClient
) : SelfUpdateParserApi {

    override fun getName(): String = "Github. Google Feature: ${isGooglePlayEnable()}"

    override suspend fun getLastUpdate(): SelfUpdate? {
        val isDev = isDev()

        val update =
//        = if (isDev) {
//            parseDevUpdate()
//        } else {
            parseReleaseUpdate()
        //  parseReleaseTest()
        // }


        val downloadUrl = update.getDownloadUrl(isGooglePlayEnable = isGooglePlayEnable())
            ?: return null

        Log.e("mt05", downloadUrl)

        val selfUpdate = SelfUpdate(
            version = update.getVersion(isDev),
            downloadUrl = downloadUrl,
            name = update.name,
            description = update.description
        )
        Log.e("mt05", selfUpdate.toString())
        return selfUpdate
    }

    private fun parseReleaseTest(): GithubRelease {
        return GithubRelease(
            "1.0.1",
            listOf(
                GithubAsset(
                    "sp-01",
                    browserDownloadUrl = "https://github.com/MarketTwits/SportsouceMobile/releases/download/1.0.1/sp-01-01.03.24.apk"
                )
            ),
            "sportsauceDebug",
            false,
            ""
        )
    }

    private suspend fun parseReleaseUpdate(): GithubRelease {
        return client.get(
            urlString = GITHUB_API_LAST_RELEASE
        ).body()
    }

    private suspend fun parseDevUpdate(): GithubRelease? {
        val response = client.get(
            urlString = GITHUB_API_ALL_RELEASES
        ).body<List<GithubRelease>>()
        Log.e("mt05", response.toString())
        return response.firstOrNull { it.tagName == "spo-01" }
    }

    private fun isGooglePlayEnable() = false

    private fun isDev(): Boolean {
        return false
    }
}
