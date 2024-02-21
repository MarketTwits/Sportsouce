package com.markettwits.profile.presentation.component.unauthorized

import com.arkivanov.decompose.value.Value
import com.markettwits.profile.presentation.deprecated.ProfileUiState

interface UnAuthorizedProfile {
    val state : Value<ProfileUiState>
    fun signIn()
}