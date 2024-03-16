package com.markettwits.cloud.di

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.cloud.api.StartsRemoteDataSourceImpl
import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.qualifier.named
import org.koin.dsl.module

val sportSouceNetworkModule = module {
    single<SportsouceApi>{
        StartsRemoteDataSourceImpl(
            get(named("sportSouce"))
        )
    }
    single<HttpClientProvider>(named("sportSouce")){
        HttpClientProviderBase(
            json = JsonProviderBase().provide(),
            clientEngine = OkHttp.create(),
            baseUrl = "https://sport-73zoq.ondigitalocean.app"
        )
    }
}
