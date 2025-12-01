package com.markettwits.sportsouce.start.presentation.result.component

import com.markettwits.sportsouce.start.presentation.result.store.StartMemberResultsStore
import kotlinx.coroutines.flow.StateFlow

interface StartMemberResultsComponent {
    val state: StateFlow<StartMemberResultsStore.State>
    val filterValue: StateFlow<String>

    fun obtainEvent(intent: StartMemberResultsStore.Intent)
    fun handleTextFiled(value: String)
}
