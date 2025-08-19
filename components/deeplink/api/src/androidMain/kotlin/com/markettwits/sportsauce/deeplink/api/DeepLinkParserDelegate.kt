package com.markettwits.sportsauce.deeplink.api

import android.content.Context
import android.content.Intent
import com.markettwits.sportsauce.deeplink.model.DeepLinkParserDelegatePriority
import com.markettwits.sportsauce.deeplink.model.Deeplink

interface DeepLinkParserDelegate {
    fun getPriority(context: Context, intent: Intent): DeepLinkParserDelegatePriority?
    suspend fun fromIntent(context: Context, intent: Intent): Deeplink?
}
