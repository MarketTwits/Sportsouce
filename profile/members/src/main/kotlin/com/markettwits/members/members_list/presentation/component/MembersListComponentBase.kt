package com.markettwits.members.members_list.presentation.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.members.common.domain.ProfileMember
import com.markettwits.members.members_list.presentation.store.store.MembersListStore
import com.markettwits.members.members_list.presentation.store.store.MembersListStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MembersListComponentBase(
    componentContext: ComponentContext,
    private val storeFactory: MembersListStoreFactory,
    private val pop: () -> Unit,
    private val goDetail: (ProfileMember) -> Unit
) : MembersListComponent,
    ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    private val store = instanceKeeper.getStore {
        storeFactory.create()
    }
    override val state: StateFlow<MembersListStore.State> = store.stateFlow
    override fun obtainEvent(intent: MembersListStore.Intent) {
        store.accept(intent)
    }

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is MembersListStore.Label.GoBack -> pop()
                    is MembersListStore.Label.OnClickAddMember -> TODO()
                    is MembersListStore.Label.OnClickMember -> goDetail(it.member)
                }
            }
        }
    }
}
