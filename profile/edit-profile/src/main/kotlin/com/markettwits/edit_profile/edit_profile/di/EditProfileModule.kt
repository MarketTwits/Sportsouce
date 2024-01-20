package com.markettwits.edit_profile.edit_profile.di

import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.profile.di.authDataSourceModule
import com.markettwits.edit_profile.edit_profile.data.EditProfileDataStore
import com.markettwits.edit_profile.edit_profile.data.EditProfileDataStoreBase
import com.markettwits.profile.presentation.component.edit_profile.presentation.mapper.RemoteUserToEditProfileMapper
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val editProfileModule = module {
    includes(sportSouceNetworkModule, authDataSourceModule)
    singleOf(::EditProfileDataStoreBase) bind EditProfileDataStore::class
    single<RemoteUserToEditProfileMapper> {
        RemoteUserToEditProfileMapper.Base()
    }
}

internal fun createEditProfileModules(dependencies: EditProfileDependencies): List<Module> {
    return listOf(
        editProfileModule,
        module {
            factory { dependencies.authDataSource }
            factory { dependencies.sportsouceApi }
        }
    )
}