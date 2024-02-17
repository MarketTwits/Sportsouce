package com.markettwits.edit_profile.social_network.presentation.component

import com.markettwits.edit_profile.social_network.presentation.store.EditProfileSocialNetworkStore
import kotlinx.coroutines.flow.StateFlow

interface EditProfileSocialNetworkComponent {
    val state: StateFlow<EditProfileSocialNetworkStore.State>
    fun obtainEvent(intent: EditProfileSocialNetworkStore.Intent)
}
