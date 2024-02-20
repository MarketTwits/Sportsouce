package com.markettwits.profile.di

import com.markettwits.change_password.di.changePasswordModule
import com.markettwits.edit_profile.edit_profile.di.editProfileModule
import com.markettwits.profile.data.BaseProfileDataSource
import com.markettwits.profile.data.ProfileDataSource
import com.markettwits.profile.domain.UserUseCase
import com.markettwits.profile.domain.UserUseCaseBase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val rootProfileModule = module {
    includes(editProfileModule,changePasswordModule, authDataSourceModule)
    singleOf(::BaseProfileDataSource) bind ProfileDataSource::class
    singleOf(::UserUseCaseBase) bind UserUseCase::class
}
