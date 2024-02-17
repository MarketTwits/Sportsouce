package com.markettwits.edit_profile.edit_profile_about.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStoreFactory
import com.markettwits.profile.di.authDataSourceModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val editProfileAboutModule = module {
    includes(sportSouceNetworkModule, authDataSourceModule)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    single<EditProfileAboutStoreFactory> {
        EditProfileAboutStoreFactory(storeFactory = get())
    }
}