package com.markettwits.profile.presentation.component.authorized.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.profile.di.authDataSourceModule
import com.markettwits.profile.presentation.component.authorized.data.AuthorizedProfileRepositoryBase
import com.markettwits.profile.presentation.component.authorized.data.mapper.AuthorizedProfileMapperBase
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStoreFactory
import com.markettwits.registrations.registrations.data.mapper.UserRegistrationsMapperBase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authorizedProfileModule = module {
    includes(authDataSourceModule, sportSouceNetworkModule)
    single<AuthorizedProfileStoreFactory> {
        AuthorizedProfileStoreFactory(
            storeFactory = get(),
            repository = AuthorizedProfileRepositoryBase(
                auth = get(),
                remote = get(),
                mapper = AuthorizedProfileMapperBase(
                    UserRegistrationsMapperBase(BaseTimeMapper()),
                    BaseTimeMapper()
                )
            )
        )
    }
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
}