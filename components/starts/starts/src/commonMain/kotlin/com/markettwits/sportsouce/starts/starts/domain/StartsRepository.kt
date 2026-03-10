package com.markettwits.sportsouce.starts.starts.domain

import com.arkivanov.decompose.value.Value
import com.markettwits.sportsouce.starts.starts.presentation.component.StartsUiState

interface StartsRepository {
    suspend fun starts(forced: Boolean, page: Int = DEFAULT_STARTS_PAGE) = Unit
    suspend fun loadNext(page: Int) = Unit
    val starts: Value<StartsUiState>

    companion object {
        const val DEFAULT_STARTS_PAGE = 1
    }
}
