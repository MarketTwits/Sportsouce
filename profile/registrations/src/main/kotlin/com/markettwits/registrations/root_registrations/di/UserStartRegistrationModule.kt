package com.markettwits.registrations.root_registrations.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.profile.di.authDataSourceModule
import com.markettwits.registrations.registrations.data.StartOrderRegistrationRepository
import com.markettwits.registrations.registrations.data.StartOrderRegistrationRepositoryBase
import com.markettwits.registrations.registrations.data.mapper.UserRegistrationsMapper
import com.markettwits.registrations.registrations.data.mapper.UserRegistrationsMapperBase
import com.markettwits.registrations.start_order_profile.store.store.StartOrderStoreFactory
import org.koin.dsl.module

val userStartRegistrationModule = module {
    includes(sportSouceNetworkModule, authDataSourceModule)

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
            repository = get()
        )
    }
}