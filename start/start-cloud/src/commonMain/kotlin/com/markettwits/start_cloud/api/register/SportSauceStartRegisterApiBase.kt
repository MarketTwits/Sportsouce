package com.markettwits.start_cloud.api.register

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.start_cloud.model.register.on_start.StartRegistrationResponse
import com.markettwits.start_cloud.model.register.price.StartRegisterPriceRequest
import com.markettwits.start_cloud.model.register.price.StartRegisterPriceResponse
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders

class SportSauceStartRegisterApiBase(
    private val httpClient: HttpClientProvider
) : SportSauceStartRegisterApi {

    private val json = httpClient.json()

    private val client = httpClient.provide(true)

    override suspend fun price(
        request: StartRegisterPriceRequest,
        token : String
    ): StartRegisterPriceResponse {
        val response = client.post("member-start/price"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(request)
        }
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun register(
        request: StartRegisterPriceRequest,
        token: String
    ): StartRegistrationResponse {
        val response = client.post("member-start"){
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(request)
        }
        return json.decodeFromString(response.body<String>())
    }
}