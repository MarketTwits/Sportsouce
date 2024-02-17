package com.markettwits.edit_profile.edit_menu.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.edit_profile.edit_menu.store.EditProfileMenuStore.Intent
import com.markettwits.edit_profile.edit_menu.store.EditProfileMenuStore.Label
import com.markettwits.edit_profile.edit_menu.store.EditProfileMenuStore.State

interface EditProfileMenuStore : Store<Intent, State, Label> {
    object State

    sealed interface Intent {
        data object GoBack : Intent
    }

    sealed interface Message

    sealed interface Label
}
