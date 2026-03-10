package com.markettwits.sportsouce.starts.starts.presentation.component

import androidx.compose.runtime.Immutable
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

@Immutable
data class StartsTabUiState(
    val items: List<StartsListItem> = emptyList(),
    val isLoading: Boolean = false,
    val isAppending: Boolean = false,
    val error: SauceError? = null,
    val endReached: Boolean = false,
    val isInitialized: Boolean = false,
)

@Immutable
sealed class StartsUiState {
    data class Success(
        val tabs: List<StartsTabUiState> = List(4) { StartsTabUiState() },
        val isRefreshing: Boolean = false,
    ) : StartsUiState()

    data class Failed(val error: SauceError) : StartsUiState()
    data object Loading : StartsUiState()
}
