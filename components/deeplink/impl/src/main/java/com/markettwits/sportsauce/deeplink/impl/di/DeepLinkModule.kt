package com.markettwits.sportsauce.deeplink.impl.di

import com.markettwits.sportsauce.deeplink.api.DeepLinkParser
import com.markettwits.sportsauce.deeplink.api.DeepLinkParserDelegate
import com.markettwits.sportsauce.deeplink.impl.DeepLinkParserImpl
import com.markettwits.sportsauce.deeplink.impl.DeepLinkSportSauce
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val deepLinkModule = module {
    // Register the SportSauce delegate
    singleOf(::DeepLinkSportSauce) bind DeepLinkParserDelegate::class
    
    // Register the parser implementation with all delegates
    single<DeepLinkParser> {
        DeepLinkParserImpl(
            delegates = getAll<DeepLinkParserDelegate>()
        )
    }
}