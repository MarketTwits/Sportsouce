package com.markettwits.sportsouce.profile.cloud.di

import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.core_cloud.provider.SportSauceBaseUrl
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkProfileApi
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkIdentityApi
import org.koin.dsl.module

val sportSauceNetworkProfileModule = module {
    single { SportSauceNetworkIdentityApi(HttpClientProviderBase(JsonProviderBase().provide(), SportSauceBaseUrl)) }
    single<SportSauceNetworkProfileApi> {
        SportSauceNetworkProfileApi(
            HttpClientProviderBase(
                json = JsonProviderBase().provide(),
                baseUrl = SportSauceBaseUrl
            )
        )
    }
}