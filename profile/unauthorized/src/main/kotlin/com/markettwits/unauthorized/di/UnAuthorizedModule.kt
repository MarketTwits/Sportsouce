package com.markettwits.unauthorized.di

import com.markettwits.profile.api.authDataSourceModule
import com.markettwits.unauthorized.domain.UnauthorizedProfileUseCase
import com.markettwits.unauthorized.domain.UnauthorizedProfileUseCaseBase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val unAuthorizedModule = module {
    includes(authDataSourceModule)
    singleOf(::UnauthorizedProfileUseCaseBase) bind UnauthorizedProfileUseCase::class
}