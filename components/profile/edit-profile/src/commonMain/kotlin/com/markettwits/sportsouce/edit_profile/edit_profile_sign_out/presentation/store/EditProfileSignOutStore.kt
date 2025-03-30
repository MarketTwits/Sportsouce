package com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.store.EditProfileSignOutStore.Intent
import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.store.EditProfileSignOutStore.Label
import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.store.EditProfileSignOutStore.State

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
