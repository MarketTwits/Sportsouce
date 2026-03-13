package com.markettwits.sportsouce.edit_profile.info.presentation.component

import com.markettwits.sportsouce.edit_profile.info.presentation.store.EditProfileInfoStore
import kotlinx.coroutines.flow.StateFlow

interface EditProfileInfoComponent {
    val state: StateFlow<EditProfileInfoStore.State>
    fun obtainEvent(intent: EditProfileInfoStore.Intent)
}
