package com.markettwits.profile.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.profile.data.AuthDataSource
import com.markettwits.profile.data.BaseAuthDataSource
import com.markettwits.profile.data.database.core.RealmDatabaseProvider
import com.markettwits.profile.data.database.data.store.AuthCacheDataSource
import com.markettwits.profile.data.mapper.SignInRemoteToCacheMapper
import com.markettwits.profile.data.mapper.SignInRemoteToUiMapper
import com.markettwits.profile.data.mapper.SignUpMapper
import com.markettwits.profile.data.mapper.SignUpMapperBase
import com.markettwits.profile.presentation.sign_up.presentation.store.SignUpStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataSourceModule = module(createdAtStart = true){
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
    //SignUp
    single<SignUpMapper> {
        SignUpMapperBase()
    }
    single<SignUpStoreFactory> {
        SignUpStoreFactory(
            storeFactory = DefaultStoreFactory(),
            registerRepository = get()
        )
    }
}
