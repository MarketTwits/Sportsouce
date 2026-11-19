package com.markettwits.sportsouce.profile.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.requireServiceSuccess
import com.markettwits.sportsouce.profile.cloud.model.registrations.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import kotlinx.serialization.json.*

class SportSauceRegistrationManagementApi(provider: HttpClientProvider) {
    private val client = provider.provide()
    private val json = Json(provider.json()) { coerceInputValues = true }

    suspend fun candidates(userId: Int, token: String): List<RegistrationCandidate> =
        read(request(HttpMethod.Get, "user/startsByUserData/$userId", token))

    suspend fun attach(ids: Set<Int>, token: String) {
        mutate(HttpMethod.Put, "user/attach-regs", token, buildJsonObject {
            putJsonArray("ids") { ids.forEach { add(it) } }
        })
    }

    suspend fun external(userId: Int, offset: Int, token: String): ExternalRegistrationPage =
        read(client.get("external-registrations") {
            bearerAuth(token)
            parameter("userId", userId)
            parameter("skipCount", offset)
            parameter("maxResultCount", ExternalPageSize)
        })

    suspend fun saveExternal(id: Int?, body: JsonObject, token: String) {
        if (id == null) mutate(HttpMethod.Post, "external-registrations", token, body)
        else mutate(HttpMethod.Put, "external-registrations/$id", token, body)
    }

    suspend fun deleteExternal(id: Int, token: String) {
        mutate(HttpMethod.Delete, "external-registrations/$id", token)
    }

    suspend fun correct(correction: RegistrationCorrection, token: String) {
        mutate(HttpMethod.Post, "identity/registrations/${correction.memberStartId}/corrections", token, buildJsonObject {
            put("type", correction.type)
            put("memberStartId", correction.memberStartId)
            put("name", correction.name)
            put("surname", correction.surname)
            put("reason", correction.reason)
            correction.birthYear?.let { put("birthYear", it) }
        })
    }

    suspend fun transferOptions(memberId: Int, token: String): RegistrationTransferOptions =
        read(request(HttpMethod.Get, "identity/registrations/$memberId/transfer-options", token))

    suspend fun recipient(memberId: Int, query: String, token: String): RegistrationTransferRecipient =
        read(request(HttpMethod.Post, "identity/registrations/$memberId/transfer-recipient", token, buildJsonObject {
            put("query", query)
        }))

    suspend fun transfer(memberId: Int, transfer: RegistrationTransfer, token: String) {
        mutate(HttpMethod.Post, "identity/registrations/$memberId/transfer", token, buildJsonObject {
            put("mode", transfer.mode)
            transfer.targetUserId?.let { put("targetUserId", it) }
            transfer.inviteEmail?.let { put("inviteEmail", it) }
            put("options", json.encodeToJsonElement(transfer.options))
        })
    }

    suspend fun outgoingTransfers(token: String): List<OutgoingRegistrationTransfer> {
        val response = read<OutgoingRegistrationTransfers>(request(HttpMethod.Get, "identity/invitations/sent", token))
        return response.rows.filter { it.isTransfer && it.status in setOf("PENDING", "AWAITING_FEE") }
    }

    suspend fun walletBalance(token: String): Int? {
        val account = read<JsonObject>(request(HttpMethod.Get, "authentication", token))
        return ((account["wallet"] as? JsonObject)?.get("balance") as? JsonPrimitive)?.intOrNull
    }

    suspend fun revokeTransfer(invitationId: Int, token: String) {
        mutate(HttpMethod.Post, "identity/invitations/$invitationId/revoke", token, buildJsonObject {})
    }

    suspend fun payTransfer(invitationId: Int, method: String, token: String): String? {
        val result = mutate(HttpMethod.Post, "identity/invitations/$invitationId/pay-fee", token, buildJsonObject {
            put("method", method)
        })
        return result.paymentUrl
    }

    suspend fun price(orderId: Int, promo: String, token: String): RegistrationPriceQuote =
        read(request(HttpMethod.Post, "member-start/$orderId/price", token, buildJsonObject {
            promo.takeIf(String::isNotEmpty)?.let { put("promocode", it) }
        }))

    suspend fun validatePromo(startId: Int, promo: String, token: String): String? {
        val result = read<JsonObject>(client.get("start/$startId/promocode") {
            bearerAuth(token)
            parameter("code", promo)
        })
        return ((result["promocode"] as? JsonObject)?.get("code") as? JsonPrimitive)?.contentOrNull
    }

    suspend fun repay(orderId: Int, promo: String, token: String): RegistrationMutation =
        mutate(HttpMethod.Put, "member-start/$orderId/repayment", token, buildJsonObject {
            put("promocode", promo)
        })

    suspend fun refundRules(startId: Int, token: String): RegistrationRefundRules =
        read(request(HttpMethod.Get, "start/$startId/refund-rules/effective", token))

    suspend fun refund(paymentGroupId: Int, percent: Int, token: String) {
        mutate(HttpMethod.Post, "member-start/payment/$paymentGroupId/yokassa-refund", token, buildJsonObject {
            put("refund_percent", percent)
        })
    }

    suspend fun options(orderId: Int, token: String): RegistrationOptions =
        read(request(HttpMethod.Get, "member-start/$orderId/unpaid-additional-fields", token))

    suspend fun payOptions(orderId: Int, body: JsonObject, token: String): RegistrationMutation =
        mutate(HttpMethod.Post, "member-start/$orderId/unpaid-additional-fields/payment", token, body)

    suspend fun changeOption(orderId: Int, answerId: Int, value: JsonObject, token: String) {
        mutate(HttpMethod.Post, "member-start/$orderId/additional-field-change-requests", token, buildJsonObject {
            put("answerId", answerId)
            put("newValue", value)
        })
    }

    suspend fun payChange(changeId: Int, token: String): RegistrationMutation =
        mutate(HttpMethod.Post, "member-start/additional-field-change-requests/$changeId/payment", token, buildJsonObject {})

    private suspend fun mutate(method: HttpMethod, path: String, token: String, body: JsonObject? = null): RegistrationMutation {
        val response = request(method, path, token, body).body<String>()
        if (response.isBlank()) return RegistrationMutation()
        val payload = json.parseToJsonElement(response)
        payload.requireServiceSuccess("Не удалось выполнить операцию с регистрацией.")
        if (payload !is JsonObject) return RegistrationMutation(payment = payload)
        val result = json.decodeFromJsonElement<RegistrationMutation>(payload)
        return result
    }

    private suspend fun request(method: HttpMethod, path: String, token: String, body: JsonObject? = null) =
        client.request(path) {
            this.method = method
            bearerAuth(token)
            if (body != null) {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        }

    private suspend inline fun <reified T> read(response: HttpResponse): T =
        json.decodeFromJsonElement(readRegistrationPayload(response, json))

    companion object {
        const val ExternalPageSize = 10
    }
}
