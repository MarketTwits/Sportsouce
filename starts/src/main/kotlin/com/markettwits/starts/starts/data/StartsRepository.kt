package com.markettwits.starts.starts.data

import com.arkivanov.decompose.value.Value
import com.markettwits.starts.starts.presentation.component.StartsUiState

interface StartsRepository {
    suspend fun starts() = Unit
    val starts : Value<StartsUiState>
}