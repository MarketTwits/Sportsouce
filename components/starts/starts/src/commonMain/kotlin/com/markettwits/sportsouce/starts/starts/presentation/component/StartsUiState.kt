package com.markettwits.sportsouce.starts.starts.presentation.component

import androidx.compose.runtime.Immutable
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

@Immutable
sealed class StartsUiState {
    class Success(
        val items: List<List<StartsListItem>>,
    ) : StartsUiState()

    class Failed(val message: String) : StartsUiState()
    data object Loading : StartsUiState()
}