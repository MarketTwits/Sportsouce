package com.markettwits.sportsouce.profile.registrations.root.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.intentActionModule
import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import com.markettwits.sportsouce.profile.registrations.detail.store.store.StartOrderStoreFactory
import com.markettwits.sportsouce.profile.registrations.list.data.StartOrderRegistrationRepository
import com.markettwits.sportsouce.profile.registrations.list.data.StartOrderRegistrationRepositoryBase
import com.markettwits.sportsouce.profile.registrations.list.data.mapper.UserRegistrationsMapper
import com.markettwits.sportsouce.profile.registrations.list.data.mapper.UserRegistrationsMapperBase
import org.koin.dsl.module

val userStartRegistrationModule = module {

    includes(
        authDataSourceModule,
        intentActionModule
    )

    single<StartOrderRegistrationRepository> {
        StartOrderRegistrationRepositoryBase(
            service = get(),
            auth = get(),
            mapper = get()
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