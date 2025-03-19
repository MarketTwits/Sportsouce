package com.markettwits.auth.cloud

import com.markettwits.auth.cloud.model.change.ChangeProfileInfoRequest
import com.markettwits.auth.cloud.model.change.ChangeProfileInfoResponse
import com.markettwits.auth.cloud.model.reset.ResetPasswordResponse
import com.markettwits.auth.cloud.model.sign_in.request.SignInRequest
import com.markettwits.auth.cloud.model.sign_in.response.SignInResponseSuccess
import com.markettwits.auth.cloud.model.sign_in.response.User
import com.markettwits.auth.cloud.model.sign_up.SignUpRequest
import com.markettwits.auth.cloud.model.sign_up.SignUpResponse
import com.markettwits.core_cloud.provider.HttpClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class SportSauceNetworkAuthApi(
    private val httpClient: HttpClientProvider
) {
    private val json = httpClient.json()

    private val client = httpClient.provide(true)

    suspend fun changeProfileInfo(
        profile: ChangeProfileInfoRequest,
        token: String
    ): ChangeProfileInfoResponse {
        val response = client.put("user/${profile.id}") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(profile)
        }
        return json.decodeFromString(response.body())
    }

    suspend fun resetPassword(email: String): ResetPasswordResponse {
        val response = client.post("authentication/reset-password") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("email" to email))
        }
        return json.decodeFromString(response.body())
    }

    suspend fun register(signUpRequest: SignUpRequest) : SignUpResponse {
        val response = client.post("authentication/register") {
            contentType(ContentType.Application.Json)
            setBody(signUpRequest)
        }
        return json.decodeFromString(response.body())
    }

    suspend fun signIn(signInRequest: SignInRequest): SignInResponseSuccess {
        val response = client.post("authentication/log-in") {
            contentType(ContentType.Application.Json)
            setBody(signInRequest)
        }
        return json.decodeFromString(response.body())
    }

    suspend fun auth(token: String): User {
        val response = client.get("authentication") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return json.decodeFromString(response.body())
    }


}