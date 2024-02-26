package com.markettwits.edit_profile.edit_social_network.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core_ui.event.EventContent
import com.markettwits.core_ui.event.StateEventWithContent
import com.markettwits.core_ui.event.consumed
import com.markettwits.edit_profile.edit_social_network.domain.UserSocialNetwork
import com.markettwits.edit_profile.edit_social_network.presentation.store.EditProfileSocialNetworkStore.Intent
import com.markettwits.edit_profile.edit_social_network.presentation.store.EditProfileSocialNetworkStore.Label
import com.markettwits.edit_profile.edit_social_network.presentation.store.EditProfileSocialNetworkStore.State

interface EditProfileSocialNetworkStore : Store<Intent, State, Label> {
    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val message: String = "",
        val data: UserSocialNetwork? = null,
        val event: StateEventWithContent<EventContent> = consumed()
    )

    sealed interface Intent {
        data object GoBack : Intent
        data class UpdateState(val userSocialNetwork: UserSocialNetwork) : Intent
        data object OnCLickUpdate : Intent
        data object OnConsumedEvent : Intent
    }

    sealed interface Message {
        data object IsLoading : Message
        data class IsFailed(val message: String) : Message
        data class IsLoaded(val userSocialNetwork: UserSocialNetwork) : Message
        data class UpdateSuccess(val message: String) : Message
        data class UpdateFailed(val message: String) : Message
        data class UpdateFiled(val userSocialNetwork: UserSocialNetwork) : Message
        data object OnConsumedEvent : Message
    }

    sealed interface Label {
        data object GoBack : Label
    }

}
