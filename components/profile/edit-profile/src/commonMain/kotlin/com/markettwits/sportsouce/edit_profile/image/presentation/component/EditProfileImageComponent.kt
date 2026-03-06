package com.markettwits.sportsouce.edit_profile.image.presentation.component

import com.markettwits.sportsouce.edit_profile.image.presentation.store.EditProfileImageStore
import kotlinx.coroutines.flow.StateFlow

interface EditProfileImageComponent {
    val state: StateFlow<EditProfileImageStore.State>
    fun obtainEvent(intent: EditProfileImageStore.Intent)
}
