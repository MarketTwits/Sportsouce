package com.markettwits.profile.authorized.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.profile.authorized.presentation.store.AuthorizedProfileStore
import com.markettwits.profile.authorized.presentation.store.AuthorizedProfileStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthorizedProfileComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: AuthorizedProfileStoreFactory,
    private val event: (AuthorizedProfileComponent.Output) -> Unit
) :
    AuthorizedProfileComponent, ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    override val state = store.stateFlow

    override fun obtainEvent(intent: AuthorizedProfileStore.Intent) {
        store.accept(intent)
    }

    override fun obtainOutput(outPut: AuthorizedProfileComponent.Output) {
        event(outPut)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    AuthorizedProfileStore.Label.GoEditProfile -> TODO()
                }
            }
        }
    }

}
