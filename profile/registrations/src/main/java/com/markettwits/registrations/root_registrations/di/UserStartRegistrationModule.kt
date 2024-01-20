package com.markettwits.registrations.root_registrations.di

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.api.StartsRemoteDataSourceImpl
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.cloud.provider.HttpClientProvider
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.profile.data.BaseAuthDataSource
import com.markettwits.profile.data.SignInRemoteToCacheMapper
import com.markettwits.profile.data.SignInRemoteToUiMapper
import com.markettwits.profile.data.database.core.RealmDatabaseProvider
import com.markettwits.profile.data.database.data.store.AuthCacheDataSource
import com.markettwits.registrations.paymant_dialog.store.RegistrationsPaymentStoreFactory
import com.markettwits.registrations.registrations.data.RegistrationsDataSource
import com.markettwits.registrations.registrations.data.RegistrationsDataSourceBase
import com.markettwits.registrations.registrations.data.RemoteRegistrationsToUiMapper
import org.koin.dsl.module
import ru.alexpanov.core_network.provider.JsonProvider

val userStartRegistrationModule = module {
    includes(sportSouceNetworkModule, )

    single<RegistrationsDataSource>{
        RegistrationsDataSourceBase(
            service = get(),
            auth = BaseAuthDataSource(
                remoteService = get(),
                local = AuthCacheDataSource(RealmDatabaseProvider.Base()),
                signInMapper = SignInRemoteToUiMapper.Base(),
                signInCacheMapper = SignInRemoteToCacheMapper.Base()
            ),
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