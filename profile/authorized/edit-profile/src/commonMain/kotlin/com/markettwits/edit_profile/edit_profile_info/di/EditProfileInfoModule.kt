package com.markettwits.edit_profile.edit_profile_info.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.edit_profile.edit_profile_info.data.EditProfileInfoCloudMapper
import com.markettwits.edit_profile.edit_profile_info.data.EditProfileInfoCloudMapperBase
import com.markettwits.edit_profile.edit_profile_info.data.EditProfileInfoRepositoryBase
import com.markettwits.edit_profile.edit_profile_info.domain.EditProfileInfoRepository
import com.markettwits.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStoreFactory
import com.markettwits.profile.api.authDataSourceModule
import com.markettwits.teams_city.di.teamsCityModule
import com.markettwits.time.ExtendedTimeMapper
import com.markettwits.time.TimeMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val editProfileInfoModule = module {
    includes(authDataSourceModule, teamsCityModule)
    singleOf(::EditProfileInfoStoreFactory)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::EditProfileInfoRepositoryBase) bind EditProfileInfoRepository::class
    singleOf(::EditProfileInfoCloudMapperBase) bind EditProfileInfoCloudMapper::class
    singleOf(::ExtendedTimeMapper) bind TimeMapper::class
}