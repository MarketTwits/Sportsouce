package com.markettwits.profile.presentation.component.authorized.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cahce.execute.base.ExecuteWithCacheBase
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.profile.api.authDataSourceModule
import com.markettwits.profile.presentation.component.authorized.data.AuthorizedProfileRepositoryBase
import com.markettwits.profile.presentation.component.authorized.data.cache.UserProfileCache
import com.markettwits.profile.presentation.component.authorized.data.mapper.AuthorizedProfileMapperBase
import com.markettwits.profile.presentation.component.authorized.domain.UserProfileInteractorBase
import com.markettwits.profile.presentation.component.authorized.presentation.store.AuthorizedProfileStoreFactory
import com.markettwits.registrations.registrations_list.data.mapper.UserRegistrationsMapperBase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authorizedProfileModule = module {
    includes(authDataSourceModule, sportSouceNetworkModule)
    single<AuthorizedProfileStoreFactory> {
        AuthorizedProfileStoreFactory(
            storeFactory = get(),
            interactor = UserProfileInteractorBase(
                repository = AuthorizedProfileRepositoryBase(
                    auth = get(),
                    remote = get(),
                    mapper = AuthorizedProfileMapperBase(
                        UserRegistrationsMapperBase(BaseTimeMapper()),
                        BaseTimeMapper()
                    ),
                    cache = UserProfileCache(),
                    executeWithCache = ExecuteWithCacheBase()
                )
            ),
        )
    }
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
}