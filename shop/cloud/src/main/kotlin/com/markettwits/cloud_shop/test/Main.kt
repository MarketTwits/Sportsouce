package com.markettwits.cloud_shop.test

import com.markettwits.cloud_shop.api.SportSauceShopApiBase
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.runBlocking

fun main() {
    val service = SportSauceShopApiBase(
        HttpClientProviderBase(
            json = JsonProviderBase().provide(),
            clientEngine = OkHttp.create(),
            //baseUrl = "https://dev-sportsauce-api-b7xxf.ondigitalocean.app"
            baseUrl = "https://sport-73zoq.ondigitalocean.app"
        )
    )
    runBlocking {
        val categories = service.categories()
        println(categories)
    }
}