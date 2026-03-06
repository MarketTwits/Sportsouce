package com.markettwits.sportsouce.edit_profile.sign_out.presentation.component

import com.markettwits.sportsouce.edit_profile.sign_out.presentation.store.EditProfileSignOutStore
import kotlinx.coroutines.flow.StateFlow

interface EditProfileSignOutComponent {
    val state: StateFlow<EditProfileSignOutStore.State>
    fun obtainEvent(intent: EditProfileSignOutStore.Intent)
}
