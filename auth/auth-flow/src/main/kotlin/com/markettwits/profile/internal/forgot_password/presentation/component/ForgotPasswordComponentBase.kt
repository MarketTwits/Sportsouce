package com.markettwits.profile.internal.forgot_password.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.profile.internal.forgot_password.presentation.store.ForgotPasswordStore
import com.markettwits.profile.internal.forgot_password.presentation.store.ForgotPasswordStoreFactory
import kotlinx.coroutines.flow.StateFlow

internal class ForgotPasswordComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: ForgotPasswordStoreFactory
) : ForgotPasswordComponent,
    ComponentContext by componentContext {
    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    override val state: StateFlow<ForgotPasswordStore.State> = store.stateFlow
    override fun obtainEvent(intent: ForgotPasswordStore.Intent) {
        store.accept(intent)
    }
}
