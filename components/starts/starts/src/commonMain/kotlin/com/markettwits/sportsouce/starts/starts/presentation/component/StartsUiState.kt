package com.markettwits.sportsouce.starts.starts.presentation.component

import androidx.compose.runtime.Immutable
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

@Immutable
sealed class StartsUiState {
    class Success(
        val items: List<List<StartsListItem>>,
    ) : StartsUiState()

    class Failed(val error: SauceError) : StartsUiState()
    data object Loading : StartsUiState()
}