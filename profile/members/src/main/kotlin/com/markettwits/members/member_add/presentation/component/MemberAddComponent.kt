package com.markettwits.members.member_add.presentation.component

import com.markettwits.members.member_add.presentation.store.MemberAddStore
import kotlinx.coroutines.flow.StateFlow

interface MemberAddComponent {
    val state: StateFlow<MemberAddStore.State>
    fun obtainEvent(intent: MemberAddStore.Intent)

}
