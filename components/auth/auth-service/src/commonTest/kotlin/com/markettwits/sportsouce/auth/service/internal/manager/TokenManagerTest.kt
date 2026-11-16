package com.markettwits.sportsouce.auth.service.internal.manager

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalEncodingApi::class)
class TokenManagerTest {
    private val tokenManager = TokenManagerBase()
    private val expectedPayload = Payload(userId = 42, role = "user", iat = 100, exp = 200)

    @Test
    fun decodesTokenWithSessionIdAndAdditionalClaims() {
        val token = tokenWithPayload(
            """{"userId":42,"role":"user","sid":"stub???","iat":100,"exp":200,"aud":"app"}""",
        )

        assertEquals(expectedPayload, tokenManager.decode(token))
    }

    @Test
    fun decodesTokenWithoutSessionId() {
        val token = tokenWithPayload("""{"userId":42,"role":"user","iat":100,"exp":200}""")

        assertEquals(expectedPayload, tokenManager.decode(token))
    }

    private fun tokenWithPayload(payload: String): String {
        val encodedPayload = Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT)
            .encode(payload.encodeToByteArray())
        return "header.$encodedPayload.signature"
    }
}
