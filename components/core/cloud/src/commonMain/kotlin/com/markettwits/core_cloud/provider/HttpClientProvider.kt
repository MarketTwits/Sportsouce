package com.markettwits.core_cloud.provider

import com.markettwits.buildkonfig.isDebugMode
import io.ktor.client.*
import kotlinx.serialization.json.Json

interface HttpClientProvider {

    fun provide(loggerEnabled: Boolean = isDebugMode): HttpClient

    fun json(): Json
}
