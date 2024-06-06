package com.markettwits.profile.internal.manager

import kotlinx.serialization.json.Json
import java.util.Base64

internal class TokenManagerBase : TokenManager {

    override fun decode(token: String): Payload {
        val parts = token.split('.')
        val payloadJson = String(Base64.getDecoder().decode(parts[1]))
        return Json.decodeFromString<Payload>(payloadJson)
    }

    override fun isExpired(expired: Long): Boolean {
        val time = System.currentTimeMillis() / MILLS_IN_SECOND
        return expired > time
    }

    private companion object {
        private const val MILLS_IN_SECOND = 1000
    }
}