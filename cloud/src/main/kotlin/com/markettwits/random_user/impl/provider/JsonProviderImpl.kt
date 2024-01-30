package com.markettwits.random_user.impl.provider

import com.markettwits.random_user.api.provider.JsonProvider
import kotlinx.serialization.json.Json

class JsonProviderImpl : JsonProvider {
    override fun get() = Json {
        isLenient = true
        ignoreUnknownKeys = true
        useAlternativeNames = false
    }
}