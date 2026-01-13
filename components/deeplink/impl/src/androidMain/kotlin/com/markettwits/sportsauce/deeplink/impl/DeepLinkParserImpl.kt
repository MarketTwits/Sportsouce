package com.markettwits.sportsauce.deeplink.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.markettwits.sportsauce.deeplink.api.DeepLinkParser
import com.markettwits.sportsauce.deeplink.api.DeepLinkParserDelegate
import com.markettwits.sportsauce.deeplink.model.Deeplink

class DeepLinkParserImpl(
    private val delegates: List<DeepLinkParserDelegate>
) : DeepLinkParser {

    override suspend fun fromUri(context: Context, uri: Uri): Deeplink? {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        return fromIntent(context, intent)
    }

    override suspend fun fromIntent(context: Context, intent: Intent): Deeplink? {
        // Sort delegates by priority (HIGH = 2, DEFAULT = 1, LOW = 0)
        val sortedDelegates = delegates
            .mapNotNull { delegate ->
                delegate.getPriority(context, intent)?.let { priority ->
                    delegate to priority
                }
            }
            .sortedByDescending { (_, priority) -> priority.ordinal }
            .map { (delegate, _) -> delegate }

        // Try each delegate until one succeeds
        for (delegate in sortedDelegates) {
            val result = delegate.fromIntent(context, intent)
            if (result != null) {
                return result
            }
        }

        return null
    }
}