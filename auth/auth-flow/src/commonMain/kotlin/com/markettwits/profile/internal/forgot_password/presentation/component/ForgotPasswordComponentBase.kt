package com.markettwits.profile.internal.forgot_password.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.profile.internal.forgot_password.presentation.store.ForgotPasswordStore
import com.markettwits.profile.internal.forgot_password.presentation.store.ForgotPasswordStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class ForgotPasswordComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: ForgotPasswordStoreFactory,
    private val pop: () -> Unit
) : ForgotPasswordComponent,
    ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ForgotPasswordStore.State> = store.stateFlow

    override fun obtainEvent(intent: ForgotPasswordStore.Intent) {
        store.accept(intent)
    }

    init {
        store.labels.onEach {
            when (it) {
                is ForgotPasswordStore.Label.OnClickBack -> pop()
            }
        }.launchIn(scope)
    }

}
