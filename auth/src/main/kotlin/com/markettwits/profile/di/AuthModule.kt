package com.markettwits.profile.di

import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.profile.data.AuthDataSource
import com.markettwits.profile.data.BaseAuthDataSource
import com.markettwits.profile.data.SignInRemoteToCacheMapper
import com.markettwits.profile.data.SignInRemoteToUiMapper
import com.markettwits.profile.data.database.core.RealmDatabaseProvider
import com.markettwits.profile.data.database.data.store.AuthCacheDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataSourceModule = module{
    includes(sportSouceNetworkModule)
    singleOf(::BaseAuthDataSource) bind AuthDataSource::class
    single<SignInRemoteToUiMapper>{
        SignInRemoteToUiMapper.Base()
    }
    single<SignInRemoteToCacheMapper> {
        SignInRemoteToCacheMapper.Base()
    }
    single{
        AuthCacheDataSource(
            realmProvider = get()
        )
    }
    single<RealmDatabaseProvider>{
        RealmDatabaseProvider.Base()
    }
}
