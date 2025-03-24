package com.markettwits.start.presentation.result.component

import com.markettwits.start.presentation.result.store.StartMemberResultsStore
import kotlinx.coroutines.flow.StateFlow

interface StartMemberResultsComponent {
    val state: StateFlow<StartMemberResultsStore.State>

    fun obtainEvent(intent: StartMemberResultsStore.Intent)
}
