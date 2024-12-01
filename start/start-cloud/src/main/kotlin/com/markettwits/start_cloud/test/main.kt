package com.markettwits.start_cloud.test

import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.start_cloud.api.start.SportSauceStartApiBase
import com.markettwits.start_cloud.di.sportsouceApiDevUrl
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.runBlocking

fun main() {
    val api = SportSauceStartApiBase(
        HttpClientProviderBase(
            JsonProviderBase().provide(),
            OkHttp.create(),
            sportsouceApiDevUrl,
        )
    )
    runBlocking {
        val start = api.start(246)
        println(start)
    }

}

private val sportsouceApiBaseUrl = "https://sport-73zoq.ondigitalocean.app"