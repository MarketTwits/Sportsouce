package com.markettwits.starts.starts.presentation.component

import androidx.compose.runtime.Immutable
import com.markettwits.starts_common.domain.StartsListItem

@Immutable
sealed class StartsUiState {
    class Success(
        val items: List<List<StartsListItem>>,
    ) : StartsUiState()
    class Failed(val message: String) : StartsUiState()
    data object Loading : StartsUiState()
}