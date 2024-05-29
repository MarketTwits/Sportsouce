package com.markettwits.profile.authorized.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cahce.execute.base.ExecuteWithCacheBase
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.crashlitics.api.di.crashlyticsModule
import com.markettwits.intentActionModule
import com.markettwits.profile.api.authDataSourceModule
import com.markettwits.profile.authorized.data.AuthorizedProfileRepositoryBase
import com.markettwits.profile.authorized.data.cache.UserProfileCache
import com.markettwits.profile.authorized.data.mapper.AuthorizedProfileMapperBase
import com.markettwits.profile.authorized.domain.UserProfileInteractorBase
import com.markettwits.profile.authorized.presentation.store.AuthorizedProfileStoreFactory
import com.markettwits.registrations.list.data.mapper.UserRegistrationsMapperBase
import com.markettwits.time.BaseTimeMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authorizedProfileModule = module {
    includes(
        authDataSourceModule,
        sportSouceNetworkModule,
        crashlyticsModule,
        intentActionModule
    )
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
            intentAction = get(),
            exceptionTracker = get()
        )
    }
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
}