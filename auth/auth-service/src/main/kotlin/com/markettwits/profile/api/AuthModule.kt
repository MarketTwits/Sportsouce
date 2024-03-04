package com.markettwits.profile.api

import com.markettwits.cahce.ObservableCache
import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.cahce.execute.base.ExecuteWithCacheBase
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.profile.internal.BaseAuthDataSource
import com.markettwits.profile.internal.database.core.RealmDatabaseProvider
import com.markettwits.profile.internal.database.data.store.AuthCacheDataSource
import com.markettwits.profile.internal.database.data.store.AuthCacheDataSourceBase
import com.markettwits.profile.internal.database.data.store.UserCache
import com.markettwits.profile.internal.manager.TokenManager
import com.markettwits.profile.internal.manager.TokenManagerBase
import com.markettwits.profile.internal.mapper.SignInRemoteToCacheMapper
import com.markettwits.profile.internal.mapper.SignInRemoteToCacheMapperBase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataSourceModule = module(){
    includes(sportSouceNetworkModule)
    singleOf(::BaseAuthDataSource) bind AuthDataSource::class
    singleOf(::TokenManagerBase) bind TokenManager::class
    singleOf(::ExecuteWithCacheBase) bind ExecuteWithCache::class
    singleOf(::SignInRemoteToCacheMapperBase) bind SignInRemoteToCacheMapper::class
    //realm
    singleOf(::AuthCacheDataSourceBase) bind AuthCacheDataSource::class
    single<ObservableCache<User>> {
        UserCache()
    }
    single<RealmDatabaseProvider>{
        RealmDatabaseProvider.Base()
    }

}
