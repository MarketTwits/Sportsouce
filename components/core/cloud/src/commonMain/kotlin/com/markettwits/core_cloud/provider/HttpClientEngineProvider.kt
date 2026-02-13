package com.markettwits.core_cloud.provider

import io.ktor.client.engine.*

expect fun defaultHttpClientEngine(): HttpClientEngineFactory<*>
