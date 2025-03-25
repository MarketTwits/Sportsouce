package com.markettwits.news.cloud

import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.core_cloud.provider.SportSauceBaseUrl
import org.koin.dsl.module

val sportSauceNewsNetworkModule = module {
    single<SportSauceNewsNetworkApi> {
        SportSauceNewsNetworkApi(
            httpClient = HttpClientProviderBase(
                json = JsonProviderBase().provide(),
                baseUrl = SportSauceBaseUrl
            ),
        )
    }
}