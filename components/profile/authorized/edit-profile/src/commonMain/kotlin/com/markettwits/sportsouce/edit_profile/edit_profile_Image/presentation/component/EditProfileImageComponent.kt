package com.markettwits.sportsouce.edit_profile.edit_profile_Image.presentation.component

import com.markettwits.sportsouce.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore
import kotlinx.coroutines.flow.StateFlow

interface EditProfileImageComponent {
    val state: StateFlow<EditProfileImageStore.State>
    fun obtainEvent(intent: EditProfileImageStore.Intent)
}
