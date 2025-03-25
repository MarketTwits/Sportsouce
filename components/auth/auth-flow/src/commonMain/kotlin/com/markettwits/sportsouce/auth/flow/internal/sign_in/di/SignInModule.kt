package com.markettwits.sportsouce.auth.flow.internal.sign_in.di

import com.markettwits.crashlitics.api.di.crashlyticsModule
import com.markettwits.sportsouce.auth.flow.internal.sign_in.domain.SignInUseCase
import com.markettwits.sportsouce.auth.flow.internal.sign_in.domain.SignInUseCaseBase
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.component.SignInInstanceKeeper
import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val signInModule = module {
    includes(authDataSourceModule, crashlyticsModule)
    singleOf(::SignInUseCaseBase) bind SignInUseCase::class
    singleOf(::SignInInstanceKeeper)
}