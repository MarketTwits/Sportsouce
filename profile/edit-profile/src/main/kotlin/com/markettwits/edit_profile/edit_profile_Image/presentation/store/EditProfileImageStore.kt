package com.markettwits.edit_profile.edit_profile_Image.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.Intent
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.Label
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.State

interface EditProfileImageStore : Store<Intent, State, Label> {
    object State

    sealed interface Intent {
        data object Dismiss : Intent
    }

    sealed interface Message

    sealed interface Label {
        data object Dismiss : Label
    }


}
