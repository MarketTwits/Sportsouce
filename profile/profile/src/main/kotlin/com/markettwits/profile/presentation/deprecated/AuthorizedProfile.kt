package com.markettwits.profile.presentation.deprecated

import com.arkivanov.decompose.value.Value

interface AuthorizedProfile {
    val profileName : Value<ProfileUiState>
    fun goEditScreen()
    fun goChangePasswordScreen()
    fun goMyRegistryScreen()
    fun goMyMembersScreen()
    fun signOut()
}