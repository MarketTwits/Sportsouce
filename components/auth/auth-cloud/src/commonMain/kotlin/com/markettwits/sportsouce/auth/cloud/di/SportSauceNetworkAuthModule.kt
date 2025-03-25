package com.markettwits.sportsouce.auth.cloud.di

import Sportsouce.components.auth.auth_cloud.BuildConfig
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.sportsouce.auth.cloud.SportSauceNetworkAuthApi
import org.koin.dsl.module

val sportSauceNetworkAuthModule = module {
    single<SportSauceNetworkAuthApi> {
        SportSauceNetworkAuthApi(
            HttpClientProviderBase(
                json = JsonProviderBase().provide(),
                baseUrl = BuildConfig.SPORTSAUCE_API_PATH
            )
        )
    }
}