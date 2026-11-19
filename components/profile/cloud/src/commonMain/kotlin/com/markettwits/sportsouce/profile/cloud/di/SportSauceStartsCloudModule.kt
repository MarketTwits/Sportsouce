package com.markettwits.sportsouce.profile.cloud.di

import com.markettwits.core_cloud.provider.HttpClientProviderBase
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.core_cloud.provider.SportSauceBaseUrl
import com.markettwits.sportsouce.profile.cloud.SportSauceRegistrationManagementApi
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkProfileApi
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkIdentityApi
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkReceiptsApi
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkPreferencesApi
import com.markettwits.sportsouce.profile.cloud.registrations.UserRegistrationsDataSource
import com.markettwits.sportsouce.profile.cloud.registrations.UserStartsRegistrationsCache
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val sportSauceNetworkProfileModule = module {
    single { UserStartsRegistrationsCache() }
    singleOf(::UserRegistrationsDataSource)
    single { SportSauceNetworkPreferencesApi(HttpClientProviderBase(JsonProviderBase().provide(), SportSauceBaseUrl)) }
    single { SportSauceNetworkReceiptsApi(HttpClientProviderBase(JsonProviderBase().provide(), SportSauceBaseUrl)) }
    single { SportSauceNetworkIdentityApi(HttpClientProviderBase(JsonProviderBase().provide(), SportSauceBaseUrl)) }
    single { SportSauceRegistrationManagementApi(HttpClientProviderBase(JsonProviderBase().provide(), SportSauceBaseUrl)) }
    single<SportSauceNetworkProfileApi> {
        SportSauceNetworkProfileApi(
            HttpClientProviderBase(
                json = JsonProviderBase().provide(),
                baseUrl = SportSauceBaseUrl
            )
        )
    }
}
