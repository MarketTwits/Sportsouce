package com.markettwits.starts.data

import com.arkivanov.decompose.value.Value
import com.markettwits.starts.presentation.StartsUiState

interface StartsRepository {
    suspend fun starts() = Unit
    suspend fun startsFilter(request : Map<String,String>) = Unit
    val starts : Value<StartsUiState>
}