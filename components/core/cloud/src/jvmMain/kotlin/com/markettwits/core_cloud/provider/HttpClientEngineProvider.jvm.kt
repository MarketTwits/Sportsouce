package com.markettwits.core_cloud.provider

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*

actual fun defaultHttpClientEngine(): HttpClientEngineFactory<*> = OkHttp
