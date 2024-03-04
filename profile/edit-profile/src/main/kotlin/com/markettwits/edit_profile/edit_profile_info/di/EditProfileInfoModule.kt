package com.markettwits.edit_profile.edit_profile_info.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.core_ui.time.BaseTimeMapper
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.edit_profile.edit_profile_info.data.EditProfileInfoCloudMapper
import com.markettwits.edit_profile.edit_profile_info.data.EditProfileInfoCloudMapperBase
import com.markettwits.edit_profile.edit_profile_info.data.EditProfileInfoRepositoryBase
import com.markettwits.edit_profile.edit_profile_info.domain.EditProfileInfoRepository
import com.markettwits.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStoreFactory
import com.markettwits.profile.api.authDataSourceModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val editProfileInfoModule = module {
    includes(sportSouceNetworkModule, authDataSourceModule)
    singleOf(::EditProfileInfoStoreFactory)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::EditProfileInfoRepositoryBase) bind EditProfileInfoRepository::class
    singleOf(::EditProfileInfoCloudMapperBase) bind EditProfileInfoCloudMapper::class
    singleOf(::BaseTimeMapper) bind TimeMapper::class
}