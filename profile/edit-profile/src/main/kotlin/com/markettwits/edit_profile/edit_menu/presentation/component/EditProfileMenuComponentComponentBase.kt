package com.markettwits.edit_profile.edit_menu.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.edit_profile.edit_menu.presentation.store.EditProfileMenuStore
import com.markettwits.edit_profile.edit_menu.presentation.store.EditProfileMenuStoreFactory
import kotlinx.coroutines.flow.StateFlow

class EditProfileMenuComponentComponentBase(
    componentContext: ComponentContext,
    private val output: (EditProfileMenuComponentComponent.OutPut) -> Unit,
    private val storeFactory: EditProfileMenuStoreFactory,
) : EditProfileMenuComponentComponent, ComponentContext by componentContext {
    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    override val state: StateFlow<EditProfileMenuStore.State> = store.stateFlow

    override fun obtainEvent(intent: EditProfileMenuStore.Intent) {
        store.accept(intent)
    }

    override fun obtainOutPut(output: EditProfileMenuComponentComponent.OutPut) {
        output(output)
    }
}
