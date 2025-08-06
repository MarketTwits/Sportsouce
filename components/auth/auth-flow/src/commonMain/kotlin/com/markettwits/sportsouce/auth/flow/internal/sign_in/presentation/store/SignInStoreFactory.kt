package com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.store

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.markettwits.crashlitics.api.tracker.ExceptionTracker
import com.markettwits.sportsouce.auth.flow.internal.sign_in.domain.SignInUseCase
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.store.SignInStore.*

class SignInStoreFactory(
    private val storeFactory: StoreFactory,
    private val useCase: SignInUseCase,
    private val exceptionTracker: ExceptionTracker,
) : Factory {

    override fun create(): SignInStore =
        object : SignInStore,
            Store<Intent, State, Label> by storeFactory.create(
                name = "SignInStore",
                initialState = State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = { SignInExecutor(useCase, exceptionTracker) },
                reducer = SignInReducer
            ) {}
}