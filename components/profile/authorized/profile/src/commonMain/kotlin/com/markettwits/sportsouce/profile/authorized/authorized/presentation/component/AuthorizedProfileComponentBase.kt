package com.markettwits.sportsouce.profile.authorized.authorized.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.store.AuthorizedProfileStore
import com.markettwits.sportsouce.profile.authorized.authorized.presentation.store.AuthorizedProfileStoreFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

class AuthorizedProfileComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: AuthorizedProfileStoreFactory,
    private val event: (AuthorizedProfileComponent.Output) -> Unit
) :
    AuthorizedProfileComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = store.stateFlow

    override fun obtainEvent(intent: AuthorizedProfileStore.Intent) {
        store.accept(intent)
    }

    override fun obtainOutput(outPut: AuthorizedProfileComponent.Output) {
        event(outPut)
    }

    init {
        lifecycle.doOnStart {
            store.accept(AuthorizedProfileStore.Intent.Retry)
        }
    }
}
