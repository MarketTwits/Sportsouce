package com.markettwits.start_cloud.di

import Sportsouce.start.start_cloud.BuildConfig
import com.markettwits.start_cloud.api.start.SportSauceStartApi
import com.markettwits.start_cloud.api.start.SportSauceStartApiBase
import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.start_cloud.api.register.SportSauceStartRegisterApi
import com.markettwits.start_cloud.api.register.SportSauceStartRegisterApiBase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val sportSauceStartNetworkModule = module {
    single<SportSauceStartApi>{
        SportSauceStartApiBase(
            get(named("sportSouce"))
        )
    }
    single<SportSauceStartRegisterApi>{
        SportSauceStartRegisterApiBase(
            get(named("sportSouce"))
        )
    }
    single<HttpClientProvider>(named("sportSouce")){
        HttpClientProviderBase(
            json = JsonProviderBase().provide(),
            baseUrl = BuildConfig.SPORTSAUCE_API_PATH
        )
    }
}