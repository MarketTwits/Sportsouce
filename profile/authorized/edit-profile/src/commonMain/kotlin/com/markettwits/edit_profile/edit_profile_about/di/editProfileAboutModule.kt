package com.markettwits.edit_profile.edit_profile_about.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.profile.cloud.di.sportSauceNetworkProfileModule
import com.markettwits.edit_profile.edit_profile_about.data.EditProfileAboutCloudMapper
import com.markettwits.edit_profile.edit_profile_about.data.EditProfileAboutCloudMapperBase
import com.markettwits.edit_profile.edit_profile_about.data.EditProfileAboutRepository
import com.markettwits.edit_profile.edit_profile_about.data.EditProfileAboutRepositoryBase
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStoreFactory
import com.markettwits.profile.api.authDataSourceModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val editProfileAboutModule = module {
    includes(sportSauceNetworkProfileModule, authDataSourceModule)
    singleOf(::EditProfileAboutStoreFactory)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::EditProfileAboutRepositoryBase) bind EditProfileAboutRepository::class
    singleOf(::EditProfileAboutCloudMapperBase) bind EditProfileAboutCloudMapper::class
}