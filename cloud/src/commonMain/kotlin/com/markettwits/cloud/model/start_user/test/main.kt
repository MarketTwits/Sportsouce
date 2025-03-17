package com.markettwits.cloud.model.start_user.test

import com.markettwits.cloud.api.StartsRemoteDataSourceImpl
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase

fun main() {
    val service = StartsRemoteDataSourceImpl(
        HttpClientProviderBase(
            json = JsonProviderBase().provide(),
            baseUrl = sportsouceApiDevUrl
        )
    )

}


private val sportsouceApiBaseUrl = "https://sport-73zoq.ondigitalocean.app"
private val sportsouceApiDevUrl = "https://dev-sportsauce-api-b7xxf.ondigitalocean.app"