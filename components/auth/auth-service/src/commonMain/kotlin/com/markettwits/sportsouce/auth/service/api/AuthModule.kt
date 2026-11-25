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
import org.koin.mp.KoinPlatform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val authDataSourceModule = module {
    includes(sportSauceNetworkAuthModule)
    single {
        BaseAuthDataSource(
            remoteService = get(),
            signInCacheMapper = get(),
            userInfoCache = get(),
            tokenManager = get(),
            authCache = get(),
            beforeClear = getOrNull<AuthSessionEnding>()
                ?: KoinPlatform.getKoinOrNull()?.getOrNull<AuthSessionEnding>(),
        )
    } bind AuthDataSource::class
    singleOf(::CachedAuthSessionBase) bind CachedAuthSession::class
    singleOf(::TokenManagerBase) bind TokenManager::class
    singleOf(::ExecuteWithCacheBase) bind ExecuteWithCache::class
    singleOf(::SignInRemoteToCacheMapper) bind SignInRemoteToCacheMapper::class
    singleOf(::AuthCacheDataSourceBase) bind AuthCacheDataSource::class
    single<ObservableCache<User>> {
        UserCache()
    }
    single {
        CachedProfilePhoto(get<ObservableCache<User>>().observe()
            .map { (it?.photo as? User.Photo.WithPhoto)?.imageUrl()?.takeIf(String::isNotBlank) }
            .catch { emit(null) }
            .distinctUntilChanged())
    }
    singleOf(::UserCredentialCache)
}
