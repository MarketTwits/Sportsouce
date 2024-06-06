package com.markettwits.profile.internal.manager


internal interface TokenManager {
    fun decode(token: String): Payload
    fun isExpired(expired : Long) : Boolean
}