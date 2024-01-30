package detail.presentation

import detail.presentation.store.UserDetailStore
import kotlinx.coroutines.flow.StateFlow

interface UserDetailComponent {
    val state: StateFlow<UserDetailStore.State>
    fun obtainEvent(intent: UserDetailStore.Intent)
}
