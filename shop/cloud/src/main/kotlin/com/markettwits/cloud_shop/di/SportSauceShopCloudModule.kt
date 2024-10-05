package com.markettwits.cloud_shop.di

import com.markettwits.cloud_shop.api.SportSauceShopApi
import com.markettwits.cloud_shop.api.SportSauceShopApiBase
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

val sportSauceShopCloudModule = module {
    single<SportSauceShopApi> {
        SportSauceShopApiBase(
            HttpClientProviderBase(
                json = JsonProviderBase().provide(),
                clientEngine = OkHttp.create(),
                baseUrl = devBaseUrl
            )
        )
    }
}

private val baseUrl = "https://sport-73zoq.ondigitalocean.app"
private val devBaseUrl = "https://dev-sportsauce-api-b7xxf.ondigitalocean.app"