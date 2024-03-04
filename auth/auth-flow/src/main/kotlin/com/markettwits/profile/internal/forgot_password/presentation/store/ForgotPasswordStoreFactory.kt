package com.markettwits.profile.internal.forgot_password.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.profile.internal.forgot_password.domain.use_case.ForgotPasswordUseCase
import com.markettwits.profile.internal.forgot_password.presentation.store.ForgotPasswordStore.Intent
import com.markettwits.profile.internal.forgot_password.presentation.store.ForgotPasswordStore.Label
import com.markettwits.profile.internal.forgot_password.presentation.store.ForgotPasswordStore.State

internal class ForgotPasswordStoreFactory(
    private val storeFactory: StoreFactory,
    private val useCase: ForgotPasswordUseCase
) {
    fun create(): ForgotPasswordStore = ForgotPasswordStoreImpl()
    private inner class ForgotPasswordStoreImpl :
        ForgotPasswordStore,
        Store<Intent, State, Label> by storeFactory.create(
            name = "ForgotPasswordStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { ForgotPasswordExecutor(useCase) },
            reducer = ForgotPasswordReducer
        )
}