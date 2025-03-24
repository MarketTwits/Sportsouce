package com.markettwits.start_cloud.test

import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.start_cloud.api.start.SportSauceStartApiBase

fun main() {
    val api = SportSauceStartApiBase(
        HttpClientProviderBase(
            JsonProviderBase().provide(),
            sportsouceApiBaseUrl,
        )
    )

}

private val sportsouceApiBaseUrl = "https://sport-73zoq.ondigitalocean.app"