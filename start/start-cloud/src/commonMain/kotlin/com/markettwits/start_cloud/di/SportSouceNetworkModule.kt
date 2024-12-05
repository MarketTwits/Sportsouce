package com.markettwits.start_cloud.di

import com.markettwits.start_cloud.api.start.SportSauceStartApi
import com.markettwits.start_cloud.api.start.SportSauceStartApiBase
import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.start_cloud.api.register.SportSauceStartRegisterApi
import com.markettwits.start_cloud.api.register.SportSauceStartRegisterApiBase
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.qualifier.named
import org.koin.dsl.module

val sportSauceStartNetworkModule = module {
    single<SportSauceStartApi>{
        SportSauceStartApiBase(
            get(named("sportSouce"))
        )
    }
    single<SportSauceStartRegisterApi>{
        SportSauceStartRegisterApiBase(
            get(named("sportSouce"))
        )
    }
    single<HttpClientProvider>(named("sportSouce")){
        HttpClientProviderBase(
            json = JsonProviderBase().provide(),
            clientEngine = OkHttp.create(),
            baseUrl = sportsouceApiBaseUrl
        )
    }
}
internal val sportsouceApiBaseUrl = "https://sport-73zoq.ondigitalocean.app"
internal val sportsouceApiDevUrl = "https://dev-sportsauce-api-b7xxf.ondigitalocean.app"
