package com.markettwits.sportsouce.edit_profile.edit_profile_Image.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore
import com.markettwits.sportsouce.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditProfileImageComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: EditProfileImageStoreFactory,
    private val pop: () -> Unit
) : EditProfileImageComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<EditProfileImageStore.State> = store.stateFlow

    override fun obtainEvent(intent: EditProfileImageStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    EditProfileImageStore.Label.Dismiss -> pop()
                }
            }
        }
    }
}
