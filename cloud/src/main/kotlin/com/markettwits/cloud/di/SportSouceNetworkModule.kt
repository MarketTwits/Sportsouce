package com.markettwits.cloud.di

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.cloud.api.StartsRemoteDataSourceImpl
import com.markettwits.cloud.api.TimeApi
import com.markettwits.cloud.api.TimeApiImpl
import com.markettwits.cloud.provider.HttpClientProvider
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.alexpanov.core_network.provider.JsonProvider

val sportSouceNetworkModule = module {
    single<SportsouceApi>{
        StartsRemoteDataSourceImpl(
            get(named("sportSouce"))
        )
    }
    single<HttpClientProvider>(named("sportSouce")){
        HttpClientProvider(
            JsonProvider().get(),
            "https://sport-73zoq.ondigitalocean.app"
        )
    }
}
val timeApiNetworkModule = module {
    single<TimeApi> {
        TimeApiImpl(
            get(named("time"))
        )
    }
    single<HttpClientProvider>(named("time")) {
        HttpClientProvider(
            JsonProvider().get(),
            "https://timeapi.io"
        )
    }
}