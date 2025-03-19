package com.markettwits.starts.starts.data

import com.markettwits.starts.starts.presentation.component.StartsUiState
import com.markettwits.starts_common.domain.StartsListItem


interface StartsCloudToUiMapper {

    fun map(exception: Throwable): StartsUiState

    fun mapSuccess(items: List<List<StartsListItem>>): StartsUiState

    fun mapAll(vararg items: List<StartsListItem>): StartsUiState

    class Base : StartsCloudToUiMapper {

        override fun map(exception: Throwable): StartsUiState =
             StartsUiState.Failed(exception.message ?: "")

        override fun mapSuccess(items: List<List<StartsListItem>>): StartsUiState =
             StartsUiState.Success(items)

        override fun mapAll(vararg items: List<StartsListItem>): StartsUiState {
            val resultLists = items.map { list -> list }
            return StartsUiState.Success(resultLists)
        }
    }
}