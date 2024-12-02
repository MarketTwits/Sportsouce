package com.markettwits.cloud.model.start_user.test

import com.markettwits.cloud.api.StartsRemoteDataSourceImpl
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import kotlinx.coroutines.runBlocking

fun main() {
    val service = StartsRemoteDataSourceImpl(
        HttpClientProviderBase(
            json = JsonProviderBase().provide(),
            clientEngine = io.ktor.client.engine.okhttp.OkHttp.create(),
            baseUrl = sportsouceApiDevUrl
        )
    )
    runBlocking {
        val result = service.userRegistriesNew(
            userId = 210,
            token = devToken
        )
        println(result)
    }

}


private val sportsouceApiBaseUrl = "https://sport-73zoq.ondigitalocean.app"
private val sportsouceApiDevUrl = "https://dev-sportsauce-api-b7xxf.ondigitalocean.app"
private val devToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIzMTAsInJvbGUiOiJhZG1pbiIsImlhdCI6MTczMjY4NzM1NCwiZXhwIjoxNzMzMjkyMTU0fQ.V9x4S2TmWb9HJpRa5pR96Ncjce_JETnk4T6NYDhIHoM"
private val prodToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIzMTAsInJvbGUiOiJhZG1pbiIsImlhdCI6MTczMzA0NjczOSwiZXhwIjoxNzMzNjUxNTM5fQ.oZJbjZYm-u0inHILX88uNXWMoNAGLrMcKIX4FDk0yDU"