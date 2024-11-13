package com.markettwits.cloud_shop.di

import com.markettwits.cloud_shop.api.SportSauceShopApi
import com.markettwits.cloud_shop.api.SportSauceShopApiBase
import com.markettwits.cloud_shop.api.SportSauceShopOrderApi
import com.markettwits.cloud_shop.api.SportSauceShopOrderApiBase
import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProvider
import com.markettwits.core_cloud.provider.JsonProviderBase
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sportSauceShopCloudModule = module {

    singleOf(::HttpClientProviderBase) bind HttpClientProvider::class
    singleOf(::JsonProviderBase) bind JsonProvider::class

    single<SportSauceShopApi> {
        SportSauceShopApiBase(
            httpClient = HttpClientProviderBase(
                json = JsonProviderBase().provide(),
                clientEngine = OkHttp.create(),
                baseUrl = baseUrl
            ),
            isLoggerEnabled = true //FIXME fetch from BuildConfig
        )
    }

    single<SportSauceShopOrderApi> {
        SportSauceShopOrderApiBase(
            httpClient = HttpClientProviderBase(
                json = JsonProviderBase().provide(),
                clientEngine = OkHttp.create(),
                baseUrl = baseUrl
            ),
            isLoggerEnabled = true //FIXME fetch from BuildConfig
        )
    }
}

private val baseUrl = "https://sport-73zoq.ondigitalocean.app"
private val devBaseUrl = "https://dev-sportsauce-api-b7xxf.ondigitalocean.app"