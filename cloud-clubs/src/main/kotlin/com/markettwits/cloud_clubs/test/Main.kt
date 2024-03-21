package com.markettwits.cloud_clubs.test

import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.runBlocking

fun main() {
    val service = com.markettwits.cloud_clubs.api.SportSauceClubsApiBase(
        HttpClientProviderBase(
            json = JsonProviderBase().provide(),
            clientEngine = OkHttp.create(),
            baseUrl = "https://dev-sportsauce-api-b7xxf.ondigitalocean.app"
        )
    )
    runBlocking {
        val categories = service.trainers()
        println(categories)
    }
}