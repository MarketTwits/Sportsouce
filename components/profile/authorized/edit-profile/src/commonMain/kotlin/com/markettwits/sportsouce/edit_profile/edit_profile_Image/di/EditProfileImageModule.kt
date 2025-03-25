package com.markettwits.sportsouce.edit_profile.edit_profile_Image.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import com.markettwits.sportsouce.edit_profile.edit_profile_Image.data.EditProfileImageRepository
import com.markettwits.sportsouce.edit_profile.edit_profile_Image.data.EditProfileImageRepositoryBase
import com.markettwits.sportsouce.edit_profile.edit_profile_Image.data.mapper.EditProfileImageCloudMapper
import com.markettwits.sportsouce.edit_profile.edit_profile_Image.data.mapper.EditProfileImageCloudMapperBase
import com.markettwits.sportsouce.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStoreFactory
import com.markettwits.sportsouce.profile.cloud.di.sportSauceNetworkProfileModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val editProfileImageModule = module {

    includes(sportSauceNetworkProfileModule, authDataSourceModule)
    singleOf(::EditProfileImageStoreFactory)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::EditProfileImageRepositoryBase) bind EditProfileImageRepository::class
    singleOf(::EditProfileImageCloudMapperBase) bind EditProfileImageCloudMapper::class

}