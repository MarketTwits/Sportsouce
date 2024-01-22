package com.markettwits.start.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.cloud.di.timeApiNetworkModule
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.profile.data.AuthDataSource
import com.markettwits.profile.data.BaseAuthDataSource
import com.markettwits.profile.data.mapper.SignInRemoteToCacheMapper
import com.markettwits.profile.data.mapper.SignInRemoteToUiMapper
import com.markettwits.profile.data.database.core.RealmDatabaseProvider
import com.markettwits.profile.data.database.data.store.AuthCacheDataSource
import com.markettwits.profile.data.mapper.SignUpMapperBase
import com.markettwits.start.data.start.BaseStartDataSource
import com.markettwits.start.data.start.StartDataSource
import com.markettwits.start.data.start.StartMembersToUiMapper
import com.markettwits.start.data.start.StartMemoryCache
import com.markettwits.start.data.start.StartRemoteToUiMapper
import com.markettwits.start.presentation.start.store.StartScreenStoreFactory
import org.koin.dsl.module

val startModule = module {

    includes(sportSouceNetworkModule, timeApiNetworkModule)


    single<StartScreenStoreFactory>{
        StartScreenStoreFactory(
            storeFactory = DefaultStoreFactory(),
            service = get()
        )
    }

    single<StartDataSource> {
        BaseStartDataSource(
            service = get(),
            authService = get(),
            timeService = get(),
            mapper = get(),
            cache = get()
        )
    }
    single<StartMemoryCache> {
        StartMemoryCache()
    }
    single<StartRemoteToUiMapper> {
        StartRemoteToUiMapper.Base(
            mapper = BaseTimeMapper(),
            membersToUiMapper = StartMembersToUiMapper.Base()
        )
    }
    single<AuthDataSource> {
        BaseAuthDataSource(
            remoteService = get(),
            signInMapper = SignInRemoteToUiMapper.Base(),
            signInCacheMapper = SignInRemoteToCacheMapper.Base(),
            local = AuthCacheDataSource(RealmDatabaseProvider.Base()),
            signUpMapper = SignUpMapperBase()
        )
    }
}
