package com.markettwits.sportsouce.starts.cloud.di

import Sportsouce.components.starts.starts_cloud.BuildConfig
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.sportsouce.starts.cloud.SportSauceNetworkStartsApi
import org.koin.dsl.module

val sportSauceStartsCloudModule = module {
    single<SportSauceNetworkStartsApi> {
        SportSauceNetworkStartsApi(
            HttpClientProviderBase(
                json = JsonProviderBase().provide(),
                baseUrl = BuildConfig.SPORTSAUCE_API_PATH
            )
        )
    }
}