package com.markettwits.sportsouce.profile.results.presentation.component

import com.markettwits.sportsouce.profile.results.presentation.store.MyResultsStore
import kotlinx.coroutines.flow.StateFlow
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.profile.results.presentation.actions.*

interface MyResultsComponent {
    val actionSlot: Value<ChildSlot<*, ResultsActionComponent>>
    fun openAction(action: ResultsAction)
    val state: StateFlow<MyResultsStore.State>
    fun obtainEvent(intent: MyResultsStore.Intent)
}
