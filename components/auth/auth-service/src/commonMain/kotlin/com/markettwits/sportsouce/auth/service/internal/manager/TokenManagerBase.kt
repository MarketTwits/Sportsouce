package com.markettwits.sportsouce.auth.service.internal.manager

import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class TokenManagerBase : TokenManager {

    private val payloadDecoder = Json { ignoreUnknownKeys = true }

    @OptIn(ExperimentalEncodingApi::class)
    override fun decode(token: String): Payload {
        val encodedPayload = token.split('.')[1]
        val payloadJson = Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT_OPTIONAL)
            .decode(encodedPayload)
            .decodeToString()
        return payloadDecoder.decodeFromString<Payload>(payloadJson)
    }

    @OptIn(ExperimentalTime::class)
    override fun isExpired(expired: Long): Boolean {
        val time = Clock.System.now().epochSeconds
        return expired > time
    }

}
