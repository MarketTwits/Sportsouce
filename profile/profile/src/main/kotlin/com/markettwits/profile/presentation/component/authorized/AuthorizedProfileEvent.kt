package com.markettwits.profile.presentation.component.authorized

import com.markettwits.cloud.model.auth.sign_in.response.User

sealed interface AuthorizedProfileEvent {
    data object SignOut : AuthorizedProfileEvent
    data class EditProfile(val user : User) : AuthorizedProfileEvent
    data object ChangePasswordProfile : AuthorizedProfileEvent
    data object MyRegistries : AuthorizedProfileEvent
    data object MyMembers : AuthorizedProfileEvent
}