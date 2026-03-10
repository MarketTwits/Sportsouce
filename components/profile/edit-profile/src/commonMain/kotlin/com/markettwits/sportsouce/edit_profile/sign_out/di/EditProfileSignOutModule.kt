package com.markettwits.sportsouce.edit_profile.sign_out.di

import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import com.markettwits.sportsouce.edit_profile.sign_out.domain.SignOutUseCase
import com.markettwits.sportsouce.edit_profile.sign_out.domain.SignOutUseCaseBase
import com.markettwits.sportsouce.edit_profile.sign_out.presentation.store.EditProfileSignOutStoreFactory
import com.markettwits.sportsouce.starts.recent.di.startsRecentModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val editProfileSignOutModule = module {
    includes(authDataSourceModule, startsRecentModule)
    singleOf(::EditProfileSignOutStoreFactory)
    singleOf(::SignOutUseCaseBase) bind SignOutUseCase::class
}