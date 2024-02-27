package com.markettwits.members.member_edit.presentation.component

import com.markettwits.members.member_edit.presentation.store.MemberEditStore
import kotlinx.coroutines.flow.StateFlow

interface MemberEditComponent {
    val state: StateFlow<MemberEditStore.State>
    fun obtainEvent(intent: MemberEditStore.Intent)

    sealed interface Mode {
        data object Edit : Mode
        data object Add : Mode
    }
}
