package com.markettwits.cloud.di

import com.markettwits.cloud.api.TimeApi
import com.markettwits.cloud.api.TimeApiImpl
import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.qualifier.named
import org.koin.dsl.module

val timeApiNetworkModule = module {
    single<TimeApi> {
        TimeApiImpl(get(named("time")))
    }
    single<HttpClientProvider>(named("time")) {
        HttpClientProviderBase(
            json = JsonProviderBase().provide(),
            clientEngine = OkHttp.create(),
            baseUrl = timeApiBaseUrl
        )
    }
}

private val timeApiBaseUrl = "https://timeapi.io"