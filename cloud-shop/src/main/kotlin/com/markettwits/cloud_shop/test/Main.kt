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
            baseUrl = "https://dev-sportsauce-api-b7xxf.ondigitalocean.app"
        )
    )
    runBlocking {
        val categories = service.product("3fdda91b-8bca-11ed-0a80-028100c7ebff")
        println(categories)
    }
}