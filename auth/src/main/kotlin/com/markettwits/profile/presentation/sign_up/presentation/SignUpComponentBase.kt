package com.markettwits.profile.presentation.sign_up.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.profile.presentation.sign_up.presentation.store.SignUpStore
import com.markettwits.profile.presentation.sign_up.presentation.store.SignUpStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SignUpComponentBase(
    context: ComponentContext,
    private val storeFactory: SignUpStoreFactory,
    private val pop: () -> Unit,
    private val profile: () -> Unit
) : SignUpComponent, ComponentContext by context {
    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    override val state: StateFlow<SignUpStore.State> = store.stateFlow

    override fun obtainEvent(intent: SignUpStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    SignUpStore.Label.OnClickBack -> pop()
                    SignUpStore.Label.OpenProfile -> profile()
                }
            }
        }
    }
}