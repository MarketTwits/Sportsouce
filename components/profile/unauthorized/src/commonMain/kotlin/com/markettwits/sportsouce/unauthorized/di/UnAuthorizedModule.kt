package com.markettwits.sportsouce.unauthorized.di

import com.markettwits.sportsouce.auth.service.api.authDataSourceModule
import com.markettwits.sportsouce.unauthorized.domain.UnauthorizedProfileUseCase
import com.markettwits.sportsouce.unauthorized.domain.UnauthorizedProfileUseCaseBase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import com.markettwits.buildkonfig.BuildKonfig
import com.markettwits.sportsouce.unauthorized.data.GuestWelcomeCacheBase
import com.markettwits.sportsouce.unauthorized.data.GuestWelcomeRemoteSourceBase
import com.markettwits.sportsouce.unauthorized.data.GuestWelcomeRepositoryBase
import com.markettwits.sportsouce.unauthorized.domain.GuestWelcomeRepository
import com.markettwits.sportsouce.unauthorized.domain.LocalGuestWelcomeRepository

val unAuthorizedModule = module {
    includes(authDataSourceModule)
    singleOf(::UnauthorizedProfileUseCaseBase) bind UnauthorizedProfileUseCase::class
    single<GuestWelcomeRepository> {
        if (BuildKonfig.GUEST_PROFILE_CONFIG_URL.isBlank()) LocalGuestWelcomeRepository
        else GuestWelcomeRepositoryBase(BuildKonfig.GUEST_PROFILE_CONFIG_URL, GuestWelcomeCacheBase(), GuestWelcomeRemoteSourceBase())
    }
}
