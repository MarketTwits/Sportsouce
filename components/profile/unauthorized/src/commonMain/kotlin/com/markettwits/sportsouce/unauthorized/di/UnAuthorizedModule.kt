package com.markettwits.sportsouce.unauthorized.di

import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import com.markettwits.sportsouce.unauthorized.domain.UnauthorizedProfileUseCase
import com.markettwits.sportsouce.unauthorized.domain.UnauthorizedProfileUseCaseBase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val unAuthorizedModule = module {
    includes(authDataSourceModule)
    singleOf(::UnauthorizedProfileUseCaseBase) bind UnauthorizedProfileUseCase::class
}