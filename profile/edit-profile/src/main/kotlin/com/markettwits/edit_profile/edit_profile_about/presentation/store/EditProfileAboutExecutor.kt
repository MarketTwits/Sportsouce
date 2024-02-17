package com.markettwits.edit_profile.edit_profile_about.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.Intent
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.Label
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.Message
import com.markettwits.edit_profile.edit_profile_about.presentation.store.EditProfileAboutStore.State

class EditProfileAboutExecutor : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            is Intent.Dismiss -> publish(Label.Dismiss)
        }
    }

}
