package com.markettwits.starts.starts.domain

import com.arkivanov.decompose.value.Value
import com.markettwits.starts.starts.presentation.component.StartsUiState

interface StartsRepository {
    suspend fun starts(forced: Boolean) = Unit
    val starts: Value<StartsUiState>
}