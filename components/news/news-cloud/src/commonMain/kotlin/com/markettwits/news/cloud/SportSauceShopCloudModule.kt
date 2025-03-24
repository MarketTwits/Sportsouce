package com.markettwits.news.cloud

import Sportsouce.components.news.news_cloud.BuildConfig
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import org.koin.dsl.module

val sportSauceNewsNetworkModule = module {

    single<SportSauceNewsNetworkApi> {
        SportSauceNewsNetworkApi(
            httpClient = HttpClientProviderBase(
                json = JsonProviderBase().provide(),
                baseUrl = BuildConfig.SPORTSAUCE_API_PATH
            ),
        )
    }
}