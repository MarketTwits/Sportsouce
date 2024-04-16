package com.markettwits.edit_profile.edit_profile_info.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.StateEventWithContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.edit_profile.edit_profile_info.domain.models.UserData
import com.markettwits.edit_profile.edit_profile_info.domain.models.UserDataContent
import com.markettwits.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore.Intent
import com.markettwits.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore.Label
import com.markettwits.edit_profile.edit_profile_info.presentation.store.EditProfileInfoStore.State
import com.markettwits.teams_city.domain.City
import com.markettwits.teams_city.domain.Team

interface EditProfileInfoStore : Store<Intent, State, Label> {
    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val message: String = "",
        val userData: UserData? = null,
        val teams: List<Team> = emptyList(),
        val cities: List<City> = emptyList(),
        val event: StateEventWithContent<EventContent> = consumed()
    )

    sealed interface Intent {
        data object GoBack : Intent
        data class UpdateState(val userData: UserData) : Intent
        data object OnClickUpdate : Intent
        data object Retry : Intent
        data object OnConsumedEvent : Intent
    }

    sealed interface Message {
        data object IsLoading : Message
        data class IsFailed(val message: String) : Message
        data class IsLoaded(val userDataContent: UserDataContent) : Message
        data class UpdateSuccess(val message: String) : Message
        data class UpdateFailed(val message: String) : Message
        data class UpdateFiled(val userData: UserData) : Message
        data object OnConsumedEvent : Message
    }

    sealed interface Label {
        data object GoBack : Label
    }


}
