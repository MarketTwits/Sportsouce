package com.markettwits.sportsouce.profile.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.ServiceResponseException
import com.markettwits.core_cloud.provider.requireServiceSuccess
import com.markettwits.core_cloud.provider.serviceErrorMessage
import com.markettwits.sportsouce.profile.cloud.model.members.ProfileMemberRequest
import com.markettwits.sportsouce.profile.cloud.model.members.ProfileMembers
import com.markettwits.sportsouce.profile.cloud.model.members.MemberMutationResponse
import com.markettwits.sportsouce.profile.cloud.model.registrations.UserRegistration
import com.markettwits.sportsouce.profile.cloud.model.registrations.UserRegistrationsRemote
import com.markettwits.sportsouce.profile.cloud.model.start_price.StartPriceResponse
import com.markettwits.sportsouce.profile.cloud.model.start_registration.StartRegistrationResponse
import com.markettwits.sportsouce.profile.cloud.model.update.ChangePasswordRequest
import com.markettwits.sportsouce.profile.cloud.model.update.ChangeProfileInfoResponse
import com.markettwits.sportsouce.profile.cloud.model.update.UploadFileResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.buildJsonObject

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
        val rows = mutableListOf<UserRegistration>()
        val seen = hashSetOf<Any>()
        var offset = 0
        val pageSize = 1000
        while (true) {
            val response = client.get("user/startsByUserId/$userId") {
                bearerAuth(token)
                parameter("maxResultCount", pageSize)
                if (offset > 0) parameter("skipCount", offset)
            }
            val page = json.decodeFromJsonElement<UserRegistrationsRemote>(readRegistrationPayload(response, json))
            val newRows = page.rows.filter { seen.add(it.id ?: it) }
            if (newRows.isEmpty()) break
            rows += newRows
            offset += page.rows.size
            if (page.count?.let { offset >= it } ?: (page.rows.size < pageSize)) break
        }
        return rows
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
        return json.decodeFromJsonElement(readRegistrationPayload(response, json))
    }

    suspend fun repay(id : Int, token: String) : StartRegistrationResponse {
        val response = client.put("member-start/$id/repayment") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return json.decodeFromJsonElement(readRegistrationPayload(response, json))
    }

    suspend fun checkStartPrice(id: Int, token: String): StartPriceResponse {
        val response = client.post("member-start/$id/price") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(buildJsonObject {})
        }
        return json.decodeFromJsonElement(readRegistrationPayload(response, json))
    }
}


internal suspend fun readRegistrationPayload(response: HttpResponse, json: Json) =
    json.parseToJsonElement(response.body<String>()).also { payload ->
        payload.requireServiceSuccess("Не удалось получить данные регистрации.")
        if (payload is JsonObject && payload.keys.all { it in RegistrationErrorKeys } &&
            (payload["success"] as? JsonPrimitive)?.booleanOrNull != true) {
            payload.serviceErrorMessage()?.let { throw ServiceResponseException(it) }
        }
    }

private val RegistrationErrorKeys = setOf("message", "detail", "title", "error_description", "reason",
    "status", "statusCode", "ok", "success", "timestamp", "path")
