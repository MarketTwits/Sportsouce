package com.markettwits.starts.data

import com.markettwits.sportsourcedemo.all.Row
import com.markettwits.starts.StartsListItem
import com.markettwits.starts.StartsUiState


interface StartsCloudToUiMapper {
    fun map(cloud: List<Row>): StartsUiState
    fun map(exception: Throwable): StartsUiState
    fun map(actual: List<Row>, withFilter: List<Row>): StartsUiState

    class Base(private val timeMapper: TimeMapper) : StartsCloudToUiMapper {
        override fun map(cloud: List<Row>): StartsUiState {
            val item = cloud.map {
                StartsListItem(
                    it.id,
                    it.name,
                    it.posterLinkFile?.fullPath ?: "",
                    timeMapper.mapTime(TimePattern.ddMMMMyyyy, it.start_date),
                    statusCode = StartsListItem.StatusCode(
                        it.start_status.code,
                        it.start_status.name
                    ),
                    it.coordinates,
                    it.condition_short ?: ""
                )
            }
            return StartsUiState.Success(listOf(item, item, item))
        }

        override fun map(exception: Throwable): StartsUiState {
            return StartsUiState.Failed(exception.message ?: "")
        }

        override fun map(actual: List<Row>, withFilter: List<Row>): StartsUiState {
            val item = actual.map {
                StartsListItem(
                    it.id,
                    it.name,
                    it.posterLinkFile?.fullPath ?: "",
                    timeMapper.mapTime(TimePattern.ddMMMMyyyy, it.start_date),
                    statusCode = StartsListItem.StatusCode(
                        it.start_status.code,
                        it.start_status.name
                    ),
                    it.coordinates,
                    it.condition_short ?: ""
                )
            }
            val item2 = withFilter.map {
                StartsListItem(
                    it.id,
                    it.name,
                    it.posterLinkFile?.fullPath ?: "",
                    timeMapper.mapTime(TimePattern.ddMMMMyyyy, it.start_date),
                    statusCode = StartsListItem.StatusCode(
                        it.start_status.code,
                        it.start_status.name
                    ),
                    it.coordinates,
                    it.condition_short ?: ""
                )
            }
            return StartsUiState.Success(listOf(item, item2))
        }
    }
}