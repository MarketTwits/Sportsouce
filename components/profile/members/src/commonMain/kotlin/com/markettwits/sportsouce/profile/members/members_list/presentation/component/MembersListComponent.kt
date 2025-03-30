package com.markettwits.sportsouce.profile.members.members_list.presentation.component

import com.markettwits.sportsouce.profile.members.members_list.presentation.store.store.MembersListStore
import kotlinx.coroutines.flow.StateFlow

interface MembersListComponent {
    val state: StateFlow<MembersListStore.State>
    fun obtainEvent(intent: MembersListStore.Intent)
}
