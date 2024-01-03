package com.markettwits.change_password.presentation.change_password.screen

import com.markettwits.core_ui.base.Event


sealed interface ChangePasswordEvent : Event {
    val message : String
    data class ShowError(override val message : String) : ChangePasswordEvent
    data class ShowSuccess(override val message: String) : ChangePasswordEvent
}