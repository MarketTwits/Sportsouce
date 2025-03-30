package com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.component

import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.store.EditProfileSignOutStore
import kotlinx.coroutines.flow.StateFlow

interface EditProfileSignOutComponent {
    val state: StateFlow<EditProfileSignOutStore.State>
    fun obtainEvent(intent: EditProfileSignOutStore.Intent)
}
