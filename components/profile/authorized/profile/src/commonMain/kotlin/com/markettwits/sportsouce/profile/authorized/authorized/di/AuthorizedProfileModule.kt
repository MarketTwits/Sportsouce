package com.markettwits.sportsouce.profile.authorized.authorized.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cahce.execute.base.ExecuteWithCacheBase
import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.crashlitics.api.di.crashlyticsModule
import com.markettwits.intentActionModule
import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import com.markettwits.sportsouce.profile.authorized.authorized.data.AuthorizedProfileRepositoryBase
import com.markettwits.sportsouce.profile.authorized.authorized.data.cache.UserProfileCache
import com.markettwits.sportsouce.profile.authorized.authorized.data.mapper.AuthorizedProfileMapperBase
import com.markettwits.sportsouce.profile.authorized.authorized.domain.UserProfileInteractorBase
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.store.AuthorizedProfileStoreFactory
import com.markettwits.sportsouce.profile.cloud.di.sportSauceNetworkProfileModule
import com.markettwits.sportsouce.profile.registrations.list.data.mapper.UserRegistrationsMapperBase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authorizedProfileModule = module {
    includes(
        authDataSourceModule,
        sportSauceNetworkProfileModule,
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