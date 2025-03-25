package com.markettwits.sportsouce.auth.service.internal.manager

import io.ktor.utils.io.core.toByteArray
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

internal class TokenManagerBase : TokenManager {

    @OptIn(ExperimentalEncodingApi::class)
    override fun decode(token: String): Payload {
        val parts = token.split('.')
        val payloadBase64 = parts[1].let {
            val rem = it.length % 4
            if (rem > 0) {
                it + "=".repeat(4 - rem)
            } else {
                it
            }
        }
        val payloadJson = Base64.decode(payloadBase64.toByteArray()).decodeToString()
        return Json.decodeFromString<Payload>(payloadJson)
    }

    override fun isExpired(expired: Long): Boolean {
        val time = Clock.System.now().epochSeconds
        return expired > time
    }

}