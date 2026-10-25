package com.markettwits.sportsouce.start.support.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.markettwits.core.decompose.componentScope
import com.markettwits.sportsouce.start.support.presentation.store.StartSupportStore
import com.markettwits.sportsouce.start.support.presentation.store.StartSupportStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn

class StartSupportComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: StartSupportStoreFactory,
    private val onSignIn: () -> Unit = {},
) : StartSupportComponent,
    ComponentContext by componentContext {
    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<StartSupportStore.State> = store.stateFlow

    init {
        lifecycle.doOnResume { store.accept(StartSupportStore.Intent.OnAuthReturned) }
        store.labels.onEach { label ->
            when (label) {
                StartSupportStore.Label.SignIn -> onSignIn()
            }
        }.launchIn(componentScope)
    }

    override fun obtainEvent(intent: StartSupportStore.Intent) {
        store.accept(intent)
    }
}
