package com.markettwits.starts.data

import com.arkivanov.decompose.value.Value
import com.markettwits.starts.presentation.StartsUiState

interface StartsRepository {
    suspend fun starts() = Unit
    val starts : Value<StartsUiState>
}