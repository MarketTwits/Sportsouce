package com.markettwits.profile.cloud.di

import Sportsouce.components.profile.cloud.BuildConfig
import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProvider
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.profile.cloud.SportSauceNetworkProfileApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sportSauceNetworkProfileModule = module {
    singleOf(::HttpClientProviderBase) bind HttpClientProvider::class
    singleOf(::JsonProviderBase) bind JsonProvider::class
    single<SportSauceNetworkProfileApi> {
        SportSauceNetworkProfileApi(
            HttpClientProviderBase(
                json = JsonProviderBase().provide(),
                baseUrl = BuildConfig.SPORTSAUCE_API_PATH
            )
        )
    }
}