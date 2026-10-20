package com.markettwits.sportsouce.profile.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.sportsouce.profile.cloud.model.identity.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

class SportSauceNetworkIdentityApi(provider: HttpClientProvider) {
    private val client = provider.provide()
    private val json = kotlinx.serialization.json.Json(provider.json()) { coerceInputValues = true }

    suspend fun preview(request: IdentityPreviewRequest, token: String): IdentityRegistrationPreview {
        val response = client.post("identity/registration-preview") {
            bearerAuth(token); contentType(ContentType.Application.Json); setBody(request)
        }
        return json.decodeFromString(response.body())
    }

    suspend fun team(token: String): IdentityTeamResponse {
        val response = client.get("identity/team") { bearerAuth(token) }
        return json.decodeFromString(response.body())
    }

    suspend fun requestConnection(targetUserId: Int, token: String): TeamRequestResponse {
        val response = client.post("identity/team/requests") {
            bearerAuth(token); contentType(ContentType.Application.Json); setBody(CreateTeamRequest(targetUserId))
        }
        return json.decodeFromString(response.body())
    }
    suspend fun respondToRequest(requestId: Int, accept: Boolean, token: String) {
        val action = if (accept) "accept" else "reject"
        mutation(client.post("identity/team/requests/$requestId/$action") {
            bearerAuth(token); contentType(ContentType.Application.Json); setBody(kotlinx.serialization.json.buildJsonObject {})
        })
    }

    suspend fun updateRelation(userId: Int, relation: String?, token: String) {
        mutation(client.put("identity/team/people/$userId/relation") {
            bearerAuth(token); contentType(ContentType.Application.Json); setBody(UpdateTeamRelation(relation))
        })
    }

    suspend fun updateAutoAccept(trustId: Int, enabled: Boolean, token: String) {
        mutation(client.patch("identity/trusted/$trustId") {
            bearerAuth(token); contentType(ContentType.Application.Json); setBody(UpdateAutoAccept(enabled))
        })
    }

    suspend fun addTrustedPerson(userId: Int, token: String) {
        mutation(client.post("identity/trusted") {
            bearerAuth(token); contentType(ContentType.Application.Json); setBody(AddTrustedPerson(userId))
        })
    }

    private suspend fun mutation(response: io.ktor.client.statement.HttpResponse) {
        val body = response.body<String>()
        if (body.isBlank()) return
        val result = json.decodeFromString<com.markettwits.sportsouce.profile.cloud.model.members.MemberMutationResponse>(body)
        check(result.success != false) { result.message ?: "Не удалось изменить связь" }
    }

}
