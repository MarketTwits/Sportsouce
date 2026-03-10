package com.markettwits.sportsouce.edit_profile.info.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.StateEventWithContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.sportsouce.edit_profile.info.domain.models.UserData
import com.markettwits.sportsouce.edit_profile.info.domain.models.UserDataContent
import com.markettwits.sportsouce.edit_profile.info.presentation.store.EditProfileInfoStore.*
import com.markettwits.sportsouce.teams_city.domain.City
import com.markettwits.sportsouce.teams_city.domain.Team

interface EditProfileInfoStore : Store<Intent, State, Label> {
    data class State(
        val isLoading: Boolean = false,
        val error: SauceError? = null,
        val userData: UserData? = null,
        val teams: List<Team> = emptyList(),
        val cities: List<City> = emptyList(),
        val event: StateEventWithContent<EventContent> = consumed(),
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
        data class IsFailed(val sauceError: SauceError) : Message
        data class IsLoaded(val userDataContent: UserDataContent) : Message
        data class UpdateSuccess(val message: String) : Message
        data class UpdateFailed(val sauceError: SauceError) : Message
        data class UpdateFiled(val userData: UserData) : Message
        data object OnConsumedEvent : Message
    }

    sealed interface Label {
        data object GoBack : Label
    }


}
