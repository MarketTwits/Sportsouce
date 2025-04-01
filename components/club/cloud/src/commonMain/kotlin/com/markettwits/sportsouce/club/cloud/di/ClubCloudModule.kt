package com.markettwits.sportsouce.club.cloud.di

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.SportSauceHttpClientProvider
import com.markettwits.sportsouce.club.cloud.api.SportSauceClubsNetworkApi
import com.markettwits.sportsouce.club.cloud.api.SportSauceClubsNetworkApiBase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val clubCloudModule = module {
    singleOf(::SportSauceClubsNetworkApiBase) bind SportSauceClubsNetworkApi::class
    factory<HttpClientProvider> {
        SportSauceHttpClientProvider()
    }
}