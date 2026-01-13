package com.markettwits.core_cloud.provider

import io.ktor.client.engine.*
import io.ktor.client.engine.js.*

actual fun defaultHttpClientEngine(): HttpClientEngineFactory<*> = Js
