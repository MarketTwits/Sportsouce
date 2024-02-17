package com.markettwits.edit_profile.edit_profile_info.presentation.component

import com.markettwits.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore
import kotlinx.coroutines.flow.StateFlow

interface EditProfileInfoComponent {
    val state: StateFlow<EditProfileInfoStore.State>
    fun obtainEvent(intent: EditProfileInfoStore.Intent)
}
