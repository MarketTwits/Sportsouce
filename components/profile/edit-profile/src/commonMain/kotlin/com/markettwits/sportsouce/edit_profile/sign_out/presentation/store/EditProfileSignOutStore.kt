package com.markettwits.sportsouce.edit_profile.sign_out.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.sportsouce.edit_profile.sign_out.presentation.store.EditProfileSignOutStore.*

interface EditProfileSignOutStore : Store<Intent, State, Label> {
    object State

    sealed interface Intent {
        data object Dismiss : Intent
        data object SignOut : Intent
    }

    sealed interface Message

    sealed interface Label {
        data object Dismiss : Label
        data object SignOut : Label
    }
}
