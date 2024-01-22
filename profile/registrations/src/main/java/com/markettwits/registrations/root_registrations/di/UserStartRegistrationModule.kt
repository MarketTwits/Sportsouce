package com.markettwits.registrations.root_registrations.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.profile.data.BaseAuthDataSource
import com.markettwits.profile.data.mapper.SignInRemoteToCacheMapper
import com.markettwits.profile.data.mapper.SignInRemoteToUiMapper
import com.markettwits.profile.data.database.core.RealmDatabaseProvider
import com.markettwits.profile.data.database.data.store.AuthCacheDataSource
import com.markettwits.profile.di.authDataSourceModule
import com.markettwits.registrations.paymant_dialog.store.RegistrationsPaymentStoreFactory
import com.markettwits.registrations.registrations.data.RegistrationsDataSource
import com.markettwits.registrations.registrations.data.RegistrationsDataSourceBase
import com.markettwits.registrations.registrations.data.RemoteRegistrationsToUiMapper
import org.koin.dsl.module

val userStartRegistrationModule = module {
    includes(sportSouceNetworkModule, authDataSourceModule)

    single<RegistrationsDataSource>{
        RegistrationsDataSourceBase(
            service = get(),
            auth = get(),
            mapper = RemoteRegistrationsToUiMapper.Base(BaseTimeMapper())
        )
    }
    single<RegistrationsPaymentStoreFactory>{
        RegistrationsPaymentStoreFactory(
            storeFactory = DefaultStoreFactory(),
            repository = get()
        )
    }
}