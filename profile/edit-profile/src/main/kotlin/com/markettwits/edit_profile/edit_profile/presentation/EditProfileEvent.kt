package com.markettwits.edit_profile.edit_profile.presentation


sealed interface EditProfileEvent {
    val message : String
    data class ShowError(override val message : String) : EditProfileEvent
    data class ShowSuccess(override val message: String) : EditProfileEvent
}