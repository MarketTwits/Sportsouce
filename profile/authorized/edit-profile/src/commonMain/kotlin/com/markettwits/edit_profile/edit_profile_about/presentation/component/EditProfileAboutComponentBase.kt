package com.markettwits.edit_profile.edit_profile_about.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditProfileAboutComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: EditProfileAboutStoreFactory,
    private val pop: () -> Unit,
) :
    EditProfileAboutComponent, ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<EditProfileAboutStore.State> = store.stateFlow

    override fun obtainEvent(intent: EditProfileAboutStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is EditProfileAboutStore.Label.Dismiss -> pop()
                }
            }
        }
    }
}
