package com.markettwits.edit_profile.edit_profile.di

import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.edit_profile.edit_profile.data.EditProfileDataStore
import com.markettwits.edit_profile.edit_profile.data.EditProfileDataStoreBase
import com.markettwits.edit_profile.edit_profile.presentation.mapper.RemoteUserToEditProfileMapper
import com.markettwits.profile.api.authDataSourceModule
import com.markettwits.teams_city.di.teamsCityModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val editProfileModule = module {
    includes(sportSouceNetworkModule, authDataSourceModule, teamsCityModule)
    singleOf(::EditProfileDataStoreBase) bind EditProfileDataStore::class
    single<RemoteUserToEditProfileMapper> {
        RemoteUserToEditProfileMapper.Base()
    }
}