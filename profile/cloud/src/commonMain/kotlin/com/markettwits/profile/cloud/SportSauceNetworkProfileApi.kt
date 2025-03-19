package com.markettwits.profile.cloud

import com.markettwits.cloud.model.start_price.StartPriceRequest
import com.markettwits.cloud.model.start_price.StartPriceResponse
import com.markettwits.cloud.model.start_registration.StartRegistrationResponse
import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.profile.cloud.model.members.ProfileMemberRequest
import com.markettwits.profile.cloud.model.members.ProfileMembers
import com.markettwits.profile.cloud.model.registrations.UserRegistration
import com.markettwits.profile.cloud.model.registrations.UserRegistrationsRemote
import com.markettwits.profile.cloud.model.update.ChangePasswordRequest
import com.markettwits.profile.cloud.model.update.ChangeProfileInfoResponse
import com.markettwits.profile.cloud.model.update.UploadFileResponse
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class SportSauceNetworkProfileApi(
    private val httpClient: HttpClientProvider
) {

    private val json = httpClient.json()

    private val client = httpClient.provide(true)

    suspend fun addMember(request: ProfileMemberRequest, token: String) {
        val response = client.post("member-template") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(request)
        }
        return json.decodeFromString(response.body())
    }

    suspend fun updateMember(memberId: Int, request: ProfileMemberRequest, token: String) {
        val response = client.put("member-template/$memberId") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(request)
        }
        return json.decodeFromString(response.body())
    }

    suspend fun deleteMember(memberId: Int, token: String) {
        val response = client.delete("member-template/$memberId") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return json.decodeFromString(response.body())
    }

    suspend fun memberTemplate(userId: Int, token: String): ProfileMembers {
        val response = client.get("member-template?user_id=$userId") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return json.decodeFromString(response.body())
    }

    suspend fun userRegistriesNew2(userId: Int, token: String): List<UserRegistration> {
        val response = client.get("user/startsByUserId/$userId?maxResultCount=1000") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return json.decodeFromString<UserRegistrationsRemote>(response.body()).rows
    }

    suspend fun changePassword(
        password: ChangePasswordRequest,
        token: String
    ): ChangeProfileInfoResponse {
        val response = client.put("authentication/change-password") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(password)
        }
        return json.decodeFromString(response.body())
    }
    suspend fun uploadFile(data: ByteArray, lastModified: Long): UploadFileResponse {
        val response = client.submitFormWithBinaryData(
            url = "file/upload",
            formData = formData {
                append("file", data, Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=uploaded_file")
                })
                append("lastModified", lastModified.toString())
            }
        )
        return json.decodeFromString(response.body())
    }

    suspend fun repay(id : Int, token: String) : StartRegistrationResponse {
        val response = client.put("member-start/$id/repayment") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return json.decodeFromString(response.body())
    }

    suspend fun checkStartPrice(
        startPriceRequest: StartPriceRequest,
        id : Int,
        token: String
    ) : StartPriceResponse {
        val response = client.post("member-start/$id/price") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(startPriceRequest)
        }
        return json.decodeFromString(StartPriceResponse.serializer(), response.body())
    }
}