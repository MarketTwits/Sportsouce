package com.markettwits.starts.data

import com.arkivanov.decompose.value.Value
import com.markettwits.starts.StartsUiState

interface StartsDataSource {
    suspend fun starts()
    val starts : Value<StartsUiState>
}