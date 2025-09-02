package com.markettwits.sportsauce.deeplink.impl

import android.content.Context
import android.content.Intent
import com.markettwits.sportsauce.deeplink.api.DeepLinkParserDelegate
import com.markettwits.sportsauce.deeplink.model.DeepLinkParserDelegatePriority
import com.markettwits.sportsauce.deeplink.model.Deeplink

private val SUPPORTED_HOSTS = listOf("sportsauce.ru", "shop.sportsauce.ru")
private const val STARTS_PATH = "starts"
private const val NEWS_PATH = "news"
private const val CLUBS_PATH = "clubs"
private const val PRODUCT_PATH = "product"

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

        val host = intent.data?.host

        // For shop.sportsauce.ru, always return HIGH priority
        if (host == "shop.sportsauce.ru") {
            return DeepLinkParserDelegatePriority.HIGH
        }

        val pathSegments = intent.data?.pathSegments
            ?: return null

        // Check if path starts with "starts", "news", or "clubs" for sportsauce.ru
        if (pathSegments.isNotEmpty()) {
            val firstPath = pathSegments.first()
            if (firstPath == STARTS_PATH || firstPath == NEWS_PATH || firstPath == CLUBS_PATH) {
                return DeepLinkParserDelegatePriority.HIGH
            }
        }

        return null
    }

    override suspend fun fromIntent(context: Context, intent: Intent): Deeplink? {
        if (!SUPPORTED_HOSTS.contains(intent.data?.host)) {
            return null
        }

        val host = intent.data?.host

        // Handle shop.sportsauce.ru URLs
        if (host == "shop.sportsauce.ru") {
            return handleShopUrls(intent.data?.pathSegments)
        }

        val pathSegments = intent.data?.pathSegments ?: return null

        if (pathSegments.isEmpty()) {
            return null
        }

        val firstPath = pathSegments.first()

        return when (firstPath) {
            STARTS_PATH -> handleStartsPath(pathSegments)
            NEWS_PATH -> handleNewsPath(pathSegments)
            CLUBS_PATH -> handleClubsPath(pathSegments)
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

    private fun handleClubsPath(pathSegments: List<String>): Deeplink? {
        // /clubs - goes to clubs list
        if (pathSegments.size == 1) {
            return Deeplink.Clubs.ClubsList
        }

        return null
    }

    private fun handleShopUrls(pathSegments: List<String>?): Deeplink? {
        if (pathSegments == null || pathSegments.isEmpty()) {
            return Deeplink.Shop.ShopRoot
        }

        val firstPath = pathSegments.first()

        return when (firstPath) {
            PRODUCT_PATH -> handleProductPath(pathSegments)
            else -> Deeplink.Shop.ShopRoot
        }
    }

    private fun handleProductPath(pathSegments: List<String>): Deeplink? {
        // /product/{productId} - goes to product detail
        if (pathSegments.size == 2) {
            val productId = pathSegments[1]
            return Deeplink.Shop.ShopProduct(productId = productId)
        }

        return null
    }
}