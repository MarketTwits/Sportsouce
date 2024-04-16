package com.markettwits.registrations.root.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.profile.api.authDataSourceModule
import com.markettwits.registrations.detail.store.store.StartOrderStoreFactory
import com.markettwits.registrations.list.data.StartOrderRegistrationRepository
import com.markettwits.registrations.list.data.StartOrderRegistrationRepositoryBase
import com.markettwits.registrations.list.data.mapper.UserRegistrationsMapper
import com.markettwits.registrations.list.data.mapper.UserRegistrationsMapperBase
import com.markettwits.time.BaseTimeMapper
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