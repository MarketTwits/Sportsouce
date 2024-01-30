package com.markettwits.random_user.api.provider

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

interface HttpClientProvider {
    fun provideClient() : HttpClient
    fun provideJson() : Json
}