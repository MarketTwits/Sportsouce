package com.markettwits.sportsouce.edit_profile.sizes.presentation.component

import com.markettwits.sportsouce.edit_profile.sizes.presentation.store.ProfileSizesStore
import kotlinx.coroutines.flow.StateFlow

interface ProfileSizesComponent {
    val state: StateFlow<ProfileSizesStore.State>
    fun obtainEvent(intent: ProfileSizesStore.Intent)
}
