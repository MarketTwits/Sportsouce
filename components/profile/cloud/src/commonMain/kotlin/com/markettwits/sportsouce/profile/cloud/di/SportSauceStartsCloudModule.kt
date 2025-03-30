package com.markettwits.sportsouce.profile.cloud.di

import com.markettwits.buildkonfig.BuildKonfig
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkProfileApi
import org.koin.dsl.module

val sportSauceNetworkProfileModule = module {
    single<SportSauceNetworkProfileApi> {
        SportSauceNetworkProfileApi(
            HttpClientProviderBase(
                json = JsonProviderBase().provide(),
                baseUrl = BuildKonfig.SPORTSAUCE_API_PATH
            )
        )
    }
}