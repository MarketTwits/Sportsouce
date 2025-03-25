package com.markettwits.sportsouce.start.cloud.di

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.SportSauceHttpClientProvider
import com.markettwits.sportsouce.start.cloud.api.register.SportSauceStartRegisterApi
import com.markettwits.sportsouce.start.cloud.api.register.SportSauceStartRegisterApiBase
import com.markettwits.sportsouce.start.cloud.api.start.SportSauceStartApi
import com.markettwits.sportsouce.start.cloud.api.start.SportSauceStartApiBase
import org.koin.core.qualifier.named
import org.koin.dsl.module

val sportSauceStartNetworkModule = module {
    single<SportSauceStartRegisterApi> {
        SportSauceStartRegisterApiBase(get(named(HTTP_CLIENT_PROVIDER_SCOPE_ID)))
    }
    single<SportSauceStartApi> {
        SportSauceStartApiBase(get(named(HTTP_CLIENT_PROVIDER_SCOPE_ID)))
    }
    single<HttpClientProvider>(named(HTTP_CLIENT_PROVIDER_SCOPE_ID)) {
        SportSauceHttpClientProvider()
    }
}

internal const val HTTP_CLIENT_PROVIDER_SCOPE_ID = "com.markettwits.sportsauce.start.cloud"