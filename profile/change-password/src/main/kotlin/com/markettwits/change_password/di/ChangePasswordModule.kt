package com.markettwits.change_password.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.change_password.data.ChangePasswordDataSource
import com.markettwits.change_password.data.ChangePasswordDataSourceBase
import com.markettwits.change_password.domain.ChangePasswordValidation
import com.markettwits.change_password.presentation.screen.ChangePasswordStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.profile.di.authDataSourceModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val changePasswordModule = module {
    includes(sportSouceNetworkModule, authDataSourceModule)

    singleOf(::ChangePasswordDataSourceBase) bind ChangePasswordDataSource::class
    singleOf(::ChangePasswordStoreFactory) bind ChangePasswordStoreFactory::class

    single<ChangePasswordValidation>{
        ChangePasswordValidation.Base()
    }
    single<StoreFactory>{
        DefaultStoreFactory()
    }

}