package com.markettwits.sportsouce.auth.service.api

import com.markettwits.cahce.ObservableCache
import com.markettwits.cahce.execute.base.ExecuteWithCache
import com.markettwits.cahce.execute.base.ExecuteWithCacheBase
import com.markettwits.sportsouce.auth.cloud.di.sportSauceNetworkAuthModule
import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User
import com.markettwits.sportsouce.auth.service.internal.BaseAuthDataSource
import com.markettwits.sportsouce.auth.service.internal.database.data.store.AuthCacheDataSource
import com.markettwits.sportsouce.auth.service.internal.database.data.store.AuthCacheDataSourceBase
import com.markettwits.sportsouce.auth.service.internal.database.data.store.UserCache
import com.markettwits.sportsouce.auth.service.internal.database.data.store.UserCredentialCache
import com.markettwits.sportsouce.auth.service.internal.manager.TokenManager
import com.markettwits.sportsouce.auth.service.internal.manager.TokenManagerBase
import com.markettwits.sportsouce.auth.service.internal.mapper.SignInRemoteToCacheMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataSourceModule = module {
    includes(sportSauceNetworkAuthModule)
    singleOf(::BaseAuthDataSource) bind AuthDataSource::class
    singleOf(::TokenManagerBase) bind TokenManager::class
    singleOf(::ExecuteWithCacheBase) bind ExecuteWithCache::class
    singleOf(::SignInRemoteToCacheMapper) bind SignInRemoteToCacheMapper::class
    singleOf(::AuthCacheDataSourceBase) bind AuthCacheDataSource::class
    single<ObservableCache<User>> {
        UserCache()
    }
    singleOf(::UserCredentialCache)
}
