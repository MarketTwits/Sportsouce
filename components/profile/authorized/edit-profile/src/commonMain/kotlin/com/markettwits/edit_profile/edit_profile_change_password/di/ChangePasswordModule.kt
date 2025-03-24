package com.markettwits.edit_profile.edit_profile_change_password.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.profile.cloud.di.sportSauceNetworkProfileModule
import com.markettwits.edit_profile.edit_profile_change_password.data.ChangePasswordDataSource
import com.markettwits.edit_profile.edit_profile_change_password.data.ChangePasswordDataSourceBase
import com.markettwits.edit_profile.edit_profile_change_password.domain.ChangePasswordValidation
import com.markettwits.edit_profile.edit_profile_change_password.presentation.screen.ChangePasswordStoreFactory
import com.markettwits.profile.api.authDataSourceModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val changePasswordModule = module {
    includes(sportSauceNetworkProfileModule, authDataSourceModule)

    singleOf(::ChangePasswordDataSourceBase) bind ChangePasswordDataSource::class
    singleOf(::ChangePasswordStoreFactory) bind ChangePasswordStoreFactory::class

    single<ChangePasswordValidation> {
        ChangePasswordValidation.Base()
    }
    single<StoreFactory> {
        DefaultStoreFactory()
    }

}