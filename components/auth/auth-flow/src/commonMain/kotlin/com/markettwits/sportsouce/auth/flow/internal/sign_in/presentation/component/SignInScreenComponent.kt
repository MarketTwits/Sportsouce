package com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.store.SignInStore
import com.markettwits.sportsouce.auth.flow.internal.sign_in.presentation.store.SignInStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignInScreenComponent(
    context: ComponentContext,
    private val storeFactory: SignInStoreFactory,
    private val toSignUp: () -> Unit,
    private val toProfile: () -> Unit,
    private val toForgotPassword: () -> Unit,
    private val toBack: () -> Unit,
) : SignInScreen, ComponentContext by context {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<SignInStore.State> = store.stateFlow

    override fun obtainEvent(intent: SignInStore.Intent) {
        store.accept(intent)
    }

    override fun back() {
        toBack()
    }

    override fun forgotPassword() {
        toForgotPassword()
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    SignInStore.Label.GoProfile -> toProfile()
                    SignInStore.Label.GoSignUp -> toSignUp()
                }
            }
        }
    }
}