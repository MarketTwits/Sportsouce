package com.markettwits.profile.internal.sign_up.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.crashlitics.api.di.crashlyticsModule
import com.markettwits.profile.api.authDataSourceModule
import com.markettwits.profile.internal.sign_up.data.SignUpMapper
import com.markettwits.profile.internal.sign_up.data.SignUpMapperBase
import com.markettwits.profile.internal.sign_up.domain.use_case.SignUpUseCase
import com.markettwits.profile.internal.sign_up.domain.use_case.SignUpUseCaseBase
import com.markettwits.profile.internal.sign_up.domain.validation.SignUpValidation
import com.markettwits.profile.internal.sign_up.domain.validation.SignUpValidationBase
import com.markettwits.profile.internal.sign_up.presentation.store.SignUpStoreFactory
import com.markettwits.time.ExtendedTimeMapper
import com.markettwits.time.TimeMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val signUpModule = module {
    includes(authDataSourceModule, crashlyticsModule)
    singleOf(::SignUpUseCaseBase) bind SignUpUseCase::class
    singleOf(::ExtendedTimeMapper) bind TimeMapper::class
    singleOf(::SignUpMapperBase) bind SignUpMapper::class
    singleOf(::SignUpValidationBase) bind SignUpValidation::class
    singleOf(::SignUpStoreFactory)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
}