package com.markettwits.profile.presentation.component.edit_profile.presentation

import com.markettwits.profile.presentation.component.edit_profile.presentation.event.Event

sealed interface EditProfileEvent : Event {
    val message : String
    data class ShowError(override val message : String) : EditProfileEvent
    data class ShowSuccess(override val message: String) : EditProfileEvent

}