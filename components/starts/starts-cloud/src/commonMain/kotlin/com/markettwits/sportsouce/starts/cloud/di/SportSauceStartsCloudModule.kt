package com.markettwits.sportsouce.starts.cloud.di

import com.markettwits.buildkonfig.BuildKonfig
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.sportsouce.starts.cloud.SportSauceNetworkStartsApi
import org.koin.dsl.module

val sportSauceStartsCloudModule = module {
    single<SportSauceNetworkStartsApi> {
        SportSauceNetworkStartsApi(
            HttpClientProviderBase(
                json = JsonProviderBase().provide(),
                baseUrl = BuildKonfig.SPORTSAUCE_API_PATH
            )
        )
    }
}