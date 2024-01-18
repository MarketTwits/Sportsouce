package com.markettwits.start.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.cloud.di.timeApiNetworkModule
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.start.data.registration.RegistrationStartRepository
import com.markettwits.start.data.registration.RegistrationStartRepositoryBase
import com.markettwits.start.data.registration.mapper.RegistrationMapper
import com.markettwits.start.data.registration.mapper.RegistrationMapperBase
import com.markettwits.start.data.registration.mapper.RegistrationRemoteToDomainMapper
import com.markettwits.start.data.registration.mapper.RegistrationRemoteToDomainMapperBase
import com.markettwits.start.data.registration.mapper.RegistrationResponseMapper
import com.markettwits.start.data.registration.mapper.RegistrationResponseMapperBase
import com.markettwits.start.presentation.registration.store.StartRegistrationStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val startRegistrationModule = module{
    includes(sportSouceNetworkModule, timeApiNetworkModule)
    single<StartRegistrationStoreFactory> {
        StartRegistrationStoreFactory(
            storeFactory = DefaultStoreFactory(),
            repository = get()
        )
    }
    single<RegistrationStartRepository>{
        RegistrationStartRepositoryBase(
            service = get(),
            authService = get(),
            statementMapper = get(),
            registerMapper = get(),
            registrationResponseMapper = get()
        )
    }
    single<TimeMapper>{
        BaseTimeMapper()
    }
    singleOf(::RegistrationRemoteToDomainMapperBase) bind RegistrationRemoteToDomainMapper::class
    singleOf(::RegistrationMapperBase) bind RegistrationMapper::class
    singleOf(::RegistrationResponseMapperBase) bind RegistrationResponseMapper::class
}