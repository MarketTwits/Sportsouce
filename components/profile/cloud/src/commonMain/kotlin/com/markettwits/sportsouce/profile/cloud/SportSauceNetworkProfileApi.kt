package com.markettwits.sportsouce.profile.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.sportsouce.profile.cloud.model.members.ProfileMemberRequest
import com.markettwits.sportsouce.profile.cloud.model.members.ProfileMembers
import com.markettwits.sportsouce.profile.cloud.model.members.MemberMutationResponse
import com.markettwits.sportsouce.profile.cloud.model.registrations.UserRegistration
import com.markettwits.sportsouce.profile.cloud.model.registrations.UserRegistrationsRemote
import com.markettwits.sportsouce.profile.cloud.model.start_price.StartPriceRequest
import com.markettwits.sportsouce.profile.cloud.model.start_price.StartPriceResponse
import com.markettwits.sportsouce.profile.cloud.model.start_registration.StartRegistrationResponse
import com.markettwits.sportsouce.profile.cloud.model.update.ChangePasswordRequest
import com.markettwits.sportsouce.profile.cloud.model.update.ChangeProfileInfoResponse
import com.markettwits.sportsouce.profile.cloud.model.update.UploadFileResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

class SportSauceNetworkProfileApi(httpClient: HttpClientProvider) {

    private val json = httpClient.json()

    private val client = httpClient.provide()

    suspend fun addMember(request: ProfileMemberRequest, token: String) {
        val response = client.post("member-template") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(request)
        }
        return readMemberMutation(response)
    }

    suspend fun updateMember(memberId: Int, request: ProfileMemberRequest, token: String) {
        val response = client.put("member-template/$memberId") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(request)
        }
        return readMemberMutation(response)
    }

    suspend fun deleteMember(memberId: Int, token: String) {
        val response = client.delete("member-template/$memberId") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return readMemberMutation(response)
    }

    suspend fun memberTemplate(userId: Int, token: String): ProfileMembers {
        val response = client.get("member-template") {
            parameter("user_id", userId)
            parameter("maxResultCount", 500)
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return json.decodeFromString(response.body())
    }

    suspend fun memberTemplateDetail(memberId: Int, token: String): ProfileMembers.ProfileMember {
        val response = client.get("member-template/$memberId") {
            bearerAuth(token)
        }
        return json.decodeFromString(response.body())
    }

    private suspend fun readMemberMutation(response: io.ktor.client.statement.HttpResponse) {
        val body = response.body<String>()
        if (body.isBlank()) return // DELETE can also respond with 204 No Content.
        val result = json.decodeFromString<MemberMutationResponse>(body)
        check(result.success != false) { result.message ?: "Не удалось сохранить изменения участника" }
    }

    suspend fun userRegistries(userId: Int, token: String): List<UserRegistration> {
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
    suspend fun uploadFile(
        data: ByteArray,
        lastModified: Long,
        fileName: String,
        contentType: String,
        token: String,
    ): UploadFileResponse {
        val response = client.submitFormWithBinaryData(
            url = "file/upload",
            formData = formData {
                append("file", data, Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                    append(HttpHeaders.ContentType, contentType)
                })
                append("lastModified", lastModified.toString())
            }
        ) {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
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
