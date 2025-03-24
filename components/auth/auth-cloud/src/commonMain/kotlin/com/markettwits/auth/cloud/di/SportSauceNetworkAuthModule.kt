package com.markettwits.auth.cloud.di

import Sportsouce.components.auth.auth_cloud.BuildConfig
import com.markettwits.auth.cloud.SportSauceNetworkAuthApi
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
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