package com.markettwits.profile.internal.forgot_password.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.cloud.di.sportSouceNetworkModule
import com.markettwits.profile.internal.forgot_password.domain.use_case.ForgotPasswordUseCase
import com.markettwits.profile.internal.forgot_password.domain.use_case.ForgotPasswordUseCaseBase
import com.markettwits.profile.internal.forgot_password.domain.validation.ForgotPasswordValidation
import com.markettwits.profile.internal.forgot_password.domain.validation.ForgotPasswordValidationBase
import com.markettwits.profile.internal.forgot_password.presentation.store.ForgotPasswordStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val forgotPasswordModule = module {
    includes(sportSouceNetworkModule)
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::ForgotPasswordStoreFactory)
    singleOf(::ForgotPasswordUseCaseBase) bind ForgotPasswordUseCase::class
    singleOf(::ForgotPasswordValidationBase) bind ForgotPasswordValidation::class
}