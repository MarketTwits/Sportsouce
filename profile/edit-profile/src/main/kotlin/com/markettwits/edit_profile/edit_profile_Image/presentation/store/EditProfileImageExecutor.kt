package com.markettwits.edit_profile.edit_profile_Image.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.Intent
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.Label
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.Message
import com.markettwits.edit_profile.edit_profile_Image.presentation.store.EditProfileImageStore.State

class EditProfileImageExecutor : CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent, getState: () -> State) {
        when (intent) {
            Intent.Dismiss -> publish(Label.Dismiss)
        }
    }

}
