package com.markettwits.profile.data

import com.auth0.android.jwt.JWT


interface TokenManager {
    fun decode(token: String): Token
    class Base : TokenManager {
        override fun decode(token: String): Token {
            val jwt = JWT(token)
            val claim = jwt.getClaim("iat").asLong() ?: 0L
            val userId = jwt.getClaim("userId").asInt() ?: 0
            val exp = jwt.getClaim("exp").asLong() ?: 0
            return Token(userId = userId, claim, exp)
        }
    }
}

data class Token(
    val userId: Int,
    val create: Long,
    val expiration: Long
)