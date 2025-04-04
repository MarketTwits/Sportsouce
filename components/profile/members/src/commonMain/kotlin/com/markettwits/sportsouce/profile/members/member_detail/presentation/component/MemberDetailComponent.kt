package com.markettwits.sportsouce.profile.members.member_detail.presentation.component

import com.markettwits.sportsouce.profile.members.member_detail.presentation.store.MemberDetailStore
import kotlinx.coroutines.flow.StateFlow

interface MemberDetailComponent {
    val state: StateFlow<MemberDetailStore.State>
    fun obtainEvent(intent: MemberDetailStore.Intent)
}
