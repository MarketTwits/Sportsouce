package com.markettwits.sportsouce.edit_profile.about.presentation.component

import com.markettwits.sportsouce.edit_profile.about.presentation.store.EditProfileAboutStore
import kotlinx.coroutines.flow.StateFlow

interface EditProfileAboutComponent {
    val state: StateFlow<EditProfileAboutStore.State>
    fun obtainEvent(intent: EditProfileAboutStore.Intent)
}
