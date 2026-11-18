package com.markettwits.sportsouce.profile.registrations.presentation.root.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.crashlitics.api.di.crashlyticsModule
import com.markettwits.intentActionModule
import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import com.markettwits.sportsouce.profile.cloud.di.sportSauceNetworkProfileModule
import com.markettwits.sportsouce.profile.registrations.data.StartOrderRegistrationRepository
import com.markettwits.sportsouce.profile.registrations.data.StartOrderRegistrationRepositoryBase
import com.markettwits.sportsouce.profile.registrations.data.UserRegistrationsDataSource
import com.markettwits.sportsouce.profile.registrations.data.cache.UserStartsRegistrationsCache
import com.markettwits.sportsouce.profile.registrations.data.mapper.UserRegistrationsMapper
import com.markettwits.sportsouce.profile.registrations.data.mapper.UserRegistrationsMapperBase
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val userStartRegistrationModule = module {

    includes(
        authDataSourceModule,
        crashlyticsModule,
        sportSauceNetworkProfileModule,
        intentActionModule
    )

    single { UserStartsRegistrationsCache() }
    singleOf(::UserRegistrationsDataSource)
    single<StartOrderRegistrationRepository> {
        StartOrderRegistrationRepositoryBase(
            service = get(),
            auth = get(),
            mapper = get(),
            registrationsDataSource = get(),
        )
    }
    single<UserRegistrationsMapper> {
        UserRegistrationsMapperBase(BaseTimeMapper())
    }
    single<StartOrderStoreFactory> {
        StartOrderStoreFactory(
            storeFactory = DefaultStoreFactory(),
            repository = get(),
            intentAction = get()
        )
    }
}
