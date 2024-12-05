package com.markettwits.club.cloud.di

import Sportsouce.club.cloud.BuildConfig
import com.markettwits.club.cloud.api.SportSauceClubsApi
import com.markettwits.club.cloud.api.SportSauceClubsApiBase
import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val clubCloudModule = module {
    singleOf(::SportSauceClubsApiBase) bind SportSauceClubsApi::class
    single<HttpClientProvider> {
        HttpClientProviderBase(
            json = JsonProviderBase().provide(),
            clientEngine = OkHttp.create(),
            baseUrl = BuildConfig.SPORTSAUCE_API_PATH
        )
    }
}