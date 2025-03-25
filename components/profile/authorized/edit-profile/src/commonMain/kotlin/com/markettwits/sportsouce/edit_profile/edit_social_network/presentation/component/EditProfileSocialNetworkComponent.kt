package com.markettwits.sportsouce.edit_profile.edit_social_network.presentation.component

import com.markettwits.sportsouce.edit_profile.edit_social_network.presentation.store.EditProfileSocialNetworkStore
import kotlinx.coroutines.flow.StateFlow

interface EditProfileSocialNetworkComponent {
    val state: StateFlow<EditProfileSocialNetworkStore.State>
    fun obtainEvent(intent: EditProfileSocialNetworkStore.Intent)
}
