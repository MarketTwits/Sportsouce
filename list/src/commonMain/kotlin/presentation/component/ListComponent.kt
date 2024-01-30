package presentation.component

import kotlinx.coroutines.flow.StateFlow
import presentation.store.ListStore

interface ListComponent {
    val state : StateFlow<ListStore.State>
    fun obtainEvent(intent: ListStore.Intent)
}