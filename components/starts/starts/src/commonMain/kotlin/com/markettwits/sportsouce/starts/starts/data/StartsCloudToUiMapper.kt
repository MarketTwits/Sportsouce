package com.markettwits.sportsouce.starts.starts.data

import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.starts.presentation.component.StartsTabUiState
import com.markettwits.sportsouce.starts.starts.presentation.component.StartsUiState


interface StartsCloudToUiMapper {

    fun map(exception: Throwable): StartsUiState

    fun mapSuccess(items: List<List<StartsListItem>>): StartsUiState

    fun mapAll(vararg items: List<StartsListItem>): StartsUiState

    class Base : StartsCloudToUiMapper {

        override fun map(exception: Throwable): StartsUiState =
            StartsUiState.Failed(exception.mapToSauceError())

        override fun mapSuccess(items: List<List<StartsListItem>>): StartsUiState =
            StartsUiState.Success(
                tabs = items.map { list ->
                    StartsTabUiState(
                        items = list,
                        isLoading = false,
                        isAppending = false,
                        error = null,
                        endReached = list.isNotEmpty(),
                        isInitialized = true,
                    )
                }
            )

        override fun mapAll(vararg items: List<StartsListItem>): StartsUiState {
            return mapSuccess(items.toList())
        }
    }
}
