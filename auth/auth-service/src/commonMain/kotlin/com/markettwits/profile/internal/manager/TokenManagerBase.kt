package com.markettwits.profile.internal.manager

import io.ktor.utils.io.core.toByteArray
import kotlinx.datetime.Clock
import kotlinx.io.bytestring.decodeToByteString
import kotlinx.io.bytestring.decodeToString
import kotlinx.serialization.json.Json
//import java.util.Base64
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

internal class TokenManagerBase : TokenManager {

//    override fun decode(token: String): Payload {
//        val parts = token.split('.')
//       // val payloadJson = String(Base64.getDecoder().decode(parts[1]))
//        return Json.decodeFromString<Payload>(payloadJson)
//    }

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

//    override fun isExpired(expired: Long): Boolean {
//        val time =  System.currentTimeMillis() / MILLS_IN_SECOND
//        return expired > time
//    }

    private companion object {
        private const val MILLS_IN_SECOND = 1000
    }
}