package com.markettwits.cloud.di

import Sportsouce.starts.starts_cloud.BuildConfig
import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProvider
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.cloud.SportSauceNetworkStartsApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sportSauceStartsCloudModule = module {
    singleOf(::HttpClientProviderBase) bind HttpClientProvider::class
    singleOf(::JsonProviderBase) bind JsonProvider::class
    single<SportSauceNetworkStartsApi> {
        SportSauceNetworkStartsApi(
            HttpClientProviderBase(
                json = JsonProviderBase().provide(),
                baseUrl = BuildConfig.SPORTSAUCE_API_PATH
            )
        )
    }
}