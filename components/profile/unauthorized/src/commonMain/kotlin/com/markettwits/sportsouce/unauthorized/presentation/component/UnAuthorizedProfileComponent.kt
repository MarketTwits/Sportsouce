package com.markettwits.sportsouce.unauthorized.presentation.component

import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.unauthorized.presentation.components.UnAuthorizedProfileUiState

interface UnAuthorizedProfileComponent {
    val state: Value<UnAuthorizedProfileUiState>
    fun signIn()
}