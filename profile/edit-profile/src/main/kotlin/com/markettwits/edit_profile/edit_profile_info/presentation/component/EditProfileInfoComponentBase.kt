package com.markettwits.edit_profile.edit_profile_info.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore
import com.markettwits.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditProfileInfoComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: EditProfileInfoStoreFactory,
    private val pop: () -> Unit
) : EditProfileInfoComponent,
    ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    override val state: StateFlow<EditProfileInfoStore.State> = store.stateFlow
    override fun obtainEvent(intent: EditProfileInfoStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    EditProfileInfoStore.Label.GoBack -> pop()
                }
            }
        }
    }
}
