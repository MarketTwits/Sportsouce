package com.markettwits.sportsouce.auth.flow.internal.forgot_password.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.markettwits.sportsouce.auth.flow.internal.forgot_password.domain.use_case.ForgotPasswordUseCase
import com.markettwits.sportsouce.auth.flow.internal.forgot_password.domain.use_case.ForgotPasswordUseCaseBase
import com.markettwits.sportsouce.auth.flow.internal.forgot_password.domain.validation.ForgotPasswordValidation
import com.markettwits.sportsouce.auth.flow.internal.forgot_password.domain.validation.ForgotPasswordValidationBase
import com.markettwits.sportsouce.auth.flow.internal.forgot_password.presentation.store.ForgotPasswordStoreFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val forgotPasswordModule = module {
    singleOf(::DefaultStoreFactory) bind StoreFactory::class
    singleOf(::ForgotPasswordStoreFactory)
    singleOf(::ForgotPasswordUseCaseBase) bind ForgotPasswordUseCase::class
    singleOf(::ForgotPasswordValidationBase) bind ForgotPasswordValidation::class
}