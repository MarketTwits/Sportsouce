package com.markettwits.sportsauce.deeplink.impl.parser.delegates

import android.content.Context
import android.content.Intent
import com.markettwits.sportsauce.deeplink.api.DeepLinkParserDelegate
import com.markettwits.sportsauce.deeplink.model.DeepLinkParserDelegatePriority
import com.markettwits.sportsauce.deeplink.model.Deeplink

private val SUPPORTED_HOSTS = listOf("sportsauce.ru")
private const val STARTS_PATH = "starts"
private const val NEWS_PATH = "news"

class DeepLinkSportSauce : DeepLinkParserDelegate {

    override fun getPriority(
        context: Context,
        intent: Intent
    ): DeepLinkParserDelegatePriority? {
        if (intent.data == null) {
            return null
        }

        if (!SUPPORTED_HOSTS.contains(intent.data?.host)) {
            return null
        }

        val pathSegments = intent.data?.pathSegments
            ?: return null

        // Check if path starts with "starts" or "news"
        if (pathSegments.isNotEmpty()) {
            val firstPath = pathSegments.first()
            if (firstPath == STARTS_PATH || firstPath == NEWS_PATH) {
                return DeepLinkParserDelegatePriority.HIGH
            }
        }

        return null
    }

    override suspend fun fromIntent(context: Context, intent: Intent): Deeplink? {
        if (!SUPPORTED_HOSTS.contains(intent.data?.host)) {
            return null
        }

        val pathSegments = intent.data?.pathSegments ?: return null

        if (pathSegments.isEmpty()) {
            return null
        }

        val firstPath = pathSegments.first()

        return when (firstPath) {
            STARTS_PATH -> handleStartsPath(pathSegments)
            NEWS_PATH -> handleNewsPath(pathSegments)
            else -> null
        }
    }

    private fun handleStartsPath(pathSegments: List<String>): Deeplink? {
        // /starts - goes to starts list
        if (pathSegments.size == 1) {
            return Deeplink.Starts.StartsList
        }

        // /starts/{id} or /starts/{slug} - goes to start detail
        if (pathSegments.size == 2) {
            val idOrSlug = pathSegments[1]

            // Try to parse as integer ID first
            val startId = idOrSlug.toIntOrNull()

            return if (startId != null) {
                Deeplink.Starts.StartDetail(startId = startId, startSlug = null)
            } else {
                Deeplink.Starts.StartDetail(startId = null, startSlug = idOrSlug)
            }
        }

        return null
    }

    private fun handleNewsPath(pathSegments: List<String>): Deeplink? {
        // /news/{id} - goes to news detail
        if (pathSegments.size == 2) {
            val newsIdStr = pathSegments[1]
            val newsId = newsIdStr.toIntOrNull()

            return if (newsId != null) {
                Deeplink.News.NewsDetail(newsId = newsId)
            } else {
                null
            }
        }

        return null
    }
}