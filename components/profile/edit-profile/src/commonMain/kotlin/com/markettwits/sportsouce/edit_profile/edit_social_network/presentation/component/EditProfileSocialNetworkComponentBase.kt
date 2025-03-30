package com.markettwits.sportsouce.edit_profile.edit_social_network.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.sportsouce.edit_profile.edit_social_network.presentation.store.EditProfileSocialNetworkStore
import com.markettwits.sportsouce.edit_profile.edit_social_network.presentation.store.EditProfileSocialNetworkStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditProfileSocialNetworkComponentBase(
    componentContext: ComponentContext,
    private val pop: () -> Unit,
    private val storeFactory: EditProfileSocialNetworkStoreFactory
) : EditProfileSocialNetworkComponent, ComponentContext by componentContext {

    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<EditProfileSocialNetworkStore.State> = store.stateFlow

    override fun obtainEvent(intent: EditProfileSocialNetworkStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    EditProfileSocialNetworkStore.Label.GoBack -> pop()
                }
            }
        }
    }
}
