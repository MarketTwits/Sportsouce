package com.markettwits.start_cloud.test

import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.start_cloud.api.start.SportSauceStartApiBase
import io.ktor.client.engine.okhttp.*
import kotlinx.coroutines.runBlocking

fun main() {
    val api = SportSauceStartApiBase(
        HttpClientProviderBase(
            JsonProviderBase().provide(),
            OkHttp.create(),
            sportsouceApiBaseUrl,
        )
    )
    runBlocking {
        val start = api.membersResults(20, 215)
        println(start)
    }

}

private val sportsouceApiBaseUrl = "https://sport-73zoq.ondigitalocean.app"