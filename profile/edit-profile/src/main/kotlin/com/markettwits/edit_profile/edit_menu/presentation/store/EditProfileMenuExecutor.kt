package com.markettwits.edit_profile.edit_menu.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.edit_profile.edit_menu.presentation.store.EditProfileMenuStore.Intent
import com.markettwits.edit_profile.edit_menu.presentation.store.EditProfileMenuStore.Label
import com.markettwits.edit_profile.edit_menu.presentation.store.EditProfileMenuStore.Message
import com.markettwits.edit_profile.edit_menu.presentation.store.EditProfileMenuStore.State

class EditProfileMenuExecutor : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            else -> TODO()
        }
    }
}
