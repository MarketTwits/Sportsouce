package com.markettwits.sportsouce.shop.cloud.di

import com.markettwits.buildkonfig.BuildKonfig
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.sportsouce.shop.cloud.api.SportSauceShopApi
import com.markettwits.sportsouce.shop.cloud.api.SportSauceShopApiBase
import com.markettwits.sportsouce.shop.cloud.api.SportSauceShopOrderApi
import com.markettwits.sportsouce.shop.cloud.api.SportSauceShopOrderApiBase
import org.koin.dsl.module

val sportSauceShopCloudModule = module {
    single<SportSauceShopApi> {
        SportSauceShopApiBase(
            httpClient = HttpClientProviderBase(
                json = JsonProviderBase().provide(),
                baseUrl = BuildKonfig.SPORTSAUCE_API_PATH
            ),
        )
    }

    single<SportSauceShopOrderApi> {
        SportSauceShopOrderApiBase(
            httpClient = HttpClientProviderBase(
                json = JsonProviderBase().provide(),
                baseUrl = BuildKonfig.SPORTSAUCE_API_PATH
            ),
        )
    }
}