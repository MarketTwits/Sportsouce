package com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.di

import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.domain.SignOutUseCase
import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.domain.SignOutUseCaseBase
import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.store.EditProfileSignOutStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val editProfileSignOutModule = module {
    includes(authDataSourceModule)
    singleOf(::EditProfileSignOutStoreFactory)
    singleOf(::SignOutUseCaseBase) bind SignOutUseCase::class
}