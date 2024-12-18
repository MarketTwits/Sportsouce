package com.markettwits.starts.starts.data

import com.markettwits.cloud.model.starts.Row
import com.markettwits.starts.starts.presentation.component.StartsUiState
import com.markettwits.starts_common.data.mapper.StartsCloudToListMapper
import com.markettwits.starts_common.domain.StartsListItem


interface StartsCloudToUiMapper {

    fun map(exception: Throwable): StartsUiState

    fun mapSuccess(items: List<List<StartsListItem>>): StartsUiState

    fun mapAll(vararg items: List<Row>): StartsUiState

    class Base(
        private val startsCloudToListMapper: StartsCloudToListMapper
    ) : StartsCloudToUiMapper {

        override fun map(exception: Throwable): StartsUiState =
             StartsUiState.Failed(exception.message ?: "")

        override fun mapSuccess(items: List<List<StartsListItem>>): StartsUiState =
             StartsUiState.Success(items)

        override fun mapAll(vararg items: List<Row>): StartsUiState {
            val resultLists = items.map { list ->
                startsCloudToListMapper.mapSingle(list)
            }
            return StartsUiState.Success(resultLists)
        }
    }
}