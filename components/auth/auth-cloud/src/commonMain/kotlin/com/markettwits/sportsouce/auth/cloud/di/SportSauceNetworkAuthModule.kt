package com.markettwits.sportsouce.auth.cloud.di

import com.markettwits.buildkonfig.BuildKonfig
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.sportsouce.auth.cloud.SportSauceNetworkAuthApi
import org.koin.dsl.module

val sportSauceNetworkAuthModule = module {
    single<SportSauceNetworkAuthApi> {
        SportSauceNetworkAuthApi(
            HttpClientProviderBase(
                json = JsonProviderBase().provide(),
                baseUrl = BuildKonfig.SPORTSAUCE_API_PATH
            )
        )
    }
}