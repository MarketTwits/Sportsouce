package com.markettwits.profile.presentation.component.authorized

import com.arkivanov.decompose.value.Value
import com.markettwits.profile.presentation.ProfileUiState

interface AuthorizedProfile {
    val profileName : Value<ProfileUiState>
    fun goEditScreen()
    fun goChangePasswordScreen()
    fun goMyRegistryScreen()
    fun goMyMembersScreen()
    fun signOut()
}