package com.markettwits.edit_profile.edit_profile_about.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.Intent
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.Label
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.State

interface EditProfileAboutStore : Store<Intent, State, Label> {
    object State
    sealed interface Intent {
        data object Dismiss : Intent
    }

    sealed interface Message

    sealed interface Label {
        data object Dismiss : Label
    }
}
