package com.markettwits.random_user.api.provider

import kotlinx.serialization.json.Json

interface JsonProvider {
    fun get() : Json
}