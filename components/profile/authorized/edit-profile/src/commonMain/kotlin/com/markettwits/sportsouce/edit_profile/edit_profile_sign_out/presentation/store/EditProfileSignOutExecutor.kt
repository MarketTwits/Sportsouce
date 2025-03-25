package com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.store

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.domain.SignOutUseCase
import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.store.EditProfileSignOutStore.Intent
import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.store.EditProfileSignOutStore.Label
import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.store.EditProfileSignOutStore.Message
import com.markettwits.sportsouce.edit_profile.edit_profile_sign_out.presentation.store.EditProfileSignOutStore.State
import kotlinx.coroutines.launch

class EditProfileSignOutExecutor(private val useCase: SignOutUseCase) :
    CoroutineExecutor<Intent, Unit, State, Message, Label>() {
    override fun executeIntent(intent: Intent) {
        when (intent) {
            Intent.Dismiss -> publish(Label.Dismiss)
            Intent.SignOut -> signOut()
        }
    }

    private fun signOut() {
        scope.launch {
            useCase.signOut().onSuccess {
                publish(Label.SignOut)
            }
        }
    }
}
