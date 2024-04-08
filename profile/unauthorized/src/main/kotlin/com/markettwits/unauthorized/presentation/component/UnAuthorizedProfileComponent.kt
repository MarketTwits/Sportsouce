package com.markettwits.unauthorized.presentation.component

import com.arkivanov.decompose.value.Value
import com.markettwits.unauthorized.presentation.components.UnAuthorizedProfileUiState

interface UnAuthorizedProfileComponent {
    val state: Value<UnAuthorizedProfileUiState>
    fun signIn()
}