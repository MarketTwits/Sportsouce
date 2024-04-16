package com.markettwits.edit_profile.edit_menu.presentation.store

import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.edit_profile.edit_menu.presentation.store.EditProfileMenuStore.Intent
import com.markettwits.edit_profile.edit_menu.presentation.store.EditProfileMenuStore.Label
import com.markettwits.edit_profile.edit_menu.presentation.store.EditProfileMenuStore.State

interface EditProfileMenuStore : Store<Intent, State, Label> {
    data object State

    sealed interface Intent {
        data object GoBack : Intent
    }

    sealed interface Message

    sealed interface Label
}
