package com.markettwits.edit_profile.edit_profile_sign_out.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.edit_profile.edit_profile_sign_out.presentation.store.EditProfileSignOutStore
import com.markettwits.edit_profile.edit_profile_sign_out.presentation.store.EditProfileSignOutStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditProfileSignOutComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: EditProfileSignOutStoreFactory,
    private val dismiss: () -> Unit,
    private val signOut: () -> Unit
) : EditProfileSignOutComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<EditProfileSignOutStore.State> = store.stateFlow

    override fun obtainEvent(intent: EditProfileSignOutStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is EditProfileSignOutStore.Label.Dismiss -> dismiss()
                    is EditProfileSignOutStore.Label.SignOut -> signOut()
                }
            }
        }
    }
}
