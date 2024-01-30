package com.markettwits.random_user.di

import com.markettwits.random_user.api.provider.HttpClientProvider
import com.markettwits.random_user.api.service.ContactsServiceApi
import com.markettwits.random_user.impl.provider.HttpClientProviderImpl
import com.markettwits.random_user.impl.provider.JsonProviderImpl
import com.markettwits.random_user.impl.service.ContactsServiceApiImpl
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val randomUserNetworkModule = module {
    single<Json> {
        JsonProviderImpl().get()
    }
    single<HttpClientProvider> {
        HttpClientProviderImpl(
            json = get(),
            baseUrl = ContactsServiceApi.RANDOM_USER_BASE_URL
        )
    }
    singleOf(::HttpClientProviderImpl) bind HttpClientProvider::class
    singleOf(::ContactsServiceApiImpl) bind ContactsServiceApi::class
}