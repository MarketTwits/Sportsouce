package com.markettwits.start.di

import com.markettwits.cloud.api.StartsRemoteDataSourceImpl
import com.markettwits.cloud.api.TimeApi
import com.markettwits.cloud.api.TimeApiImpl
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.cloud.di.timeApiNetworkModule
import com.markettwits.cloud.provider.HttpClientProvider
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.profile.data.AuthDataSource
import com.markettwits.profile.data.BaseAuthDataSource
import com.markettwits.profile.data.SignInRemoteToCacheMapper
import com.markettwits.profile.data.SignInRemoteToUiMapper
import com.markettwits.profile.data.database.core.RealmDatabaseProvider
import com.markettwits.profile.data.database.data.store.AuthCacheDataSource
import com.markettwits.start.data.BaseStartDataSource
import com.markettwits.start.data.StartDataSource
import com.markettwits.start.data.StartMembersToUiMapper
import com.markettwits.start.data.StartMemoryCache
import com.markettwits.start.data.StartRemoteToUiMapper
import org.koin.core.qualifier.named
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import ru.alexpanov.core_network.provider.JsonProvider

val startModule = module {

    includes(sportSouceNetworkModule, timeApiNetworkModule)

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
            local = AuthCacheDataSource(RealmDatabaseProvider.Base())
        )
    }
}