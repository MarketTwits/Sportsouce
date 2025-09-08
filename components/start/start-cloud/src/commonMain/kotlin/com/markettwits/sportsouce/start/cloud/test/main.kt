package com.markettwits.sportsouce.start.cloud.test

import com.markettwits.core_cloud.provider.SportSauceHttpClientProvider
import com.markettwits.sportsouce.start.cloud.api.start.SportSauceStartApiBase

suspend fun main() {
    val api = SportSauceStartApiBase(
        SportSauceHttpClientProvider(
            "https://api.sportsauce.ru"
        )
    )
    runCatching {
        api.membersResultsAnalyze(startId = 273)
    }.fold(
        onSuccess = { println(it) },
        onFailure = { println(it.message) }
    )
}