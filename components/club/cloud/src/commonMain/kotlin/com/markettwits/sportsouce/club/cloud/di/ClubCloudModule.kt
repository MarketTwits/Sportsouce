package com.markettwits.sportsouce.club.cloud.di

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.SportSauceHttpClientProvider
import com.markettwits.sportsouce.club.cloud.api.SportSauceClubsApi
import com.markettwits.sportsouce.club.cloud.api.SportSauceClubsApiBase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val clubCloudModule = module {
    singleOf(::SportSauceClubsApiBase) bind SportSauceClubsApi::class
    factory<HttpClientProvider> {
        SportSauceHttpClientProvider()
    }
}