package com.markettwits.edit_profile.edit_profile_about.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.Intent
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.Label
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.State

interface EditProfileAboutStore : Store<Intent, State, Label> {
    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val isSuccess: Boolean = false,
        val message: String = "",
        val fieldStat: FiledState = FiledState()
    ) {
        data class FiledState(
            val value: String = "",
            val isError: Boolean = false
        )
    }
    sealed interface Intent {
        data object Dismiss : Intent
        data object Apply : Intent
        data class OnValueChanged(val value: String) : Intent
    }

    sealed interface Message {
        data object Loading : Message
        data class LoadedAbout(val fieldState: State.FiledState) : Message
        data object UpdateSuccess : Message
        data class UpdateFailed(val message: String) : Message
        data class UpdateTexField(val filedState: State.FiledState) : Message
    }

    sealed interface Label {
        data object Dismiss : Label
    }
}
