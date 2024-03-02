package com.markettwits.edit_profile.edit_profile_sign_out.di

import com.markettwits.edit_profile.edit_profile_sign_out.domain.SignOutUseCase
import com.markettwits.edit_profile.edit_profile_sign_out.domain.SignOutUseCaseBase
import com.markettwits.edit_profile.edit_profile_sign_out.presentation.store.EditProfileSignOutStoreFactory
import com.markettwits.profile.di.authDataSourceModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val editProfileSignOutModule = module {
    includes(authDataSourceModule)
    singleOf(::EditProfileSignOutStoreFactory)
    singleOf(::SignOutUseCaseBase) bind SignOutUseCase::class
}