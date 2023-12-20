package com.markettwits.starts.data

import com.markettwits.sportsourcedemo.all.Row
import com.markettwits.start.core.TimeMapper
import com.markettwits.start.core.TimePattern
import com.markettwits.starts.StartsListItem
import com.markettwits.starts.StartsUiState


interface StartsCloudToUiMapper {
    @Deprecated("use map fun with varargs arguments")
    fun map(cloud: List<Row>): StartsUiState
    fun map(exception: Throwable): StartsUiState
    @Deprecated("use map fun with varargs arguments")
    fun map(actual: List<Row>, withFilter: List<Row>): StartsUiState
    fun mapAll(vararg items: List<Row>) : StartsUiState


    class Base(private val timeMapper: TimeMapper) : StartsCloudToUiMapper {
        @Deprecated("use map fun with 2 arguments")
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
            val actual = actual.map {
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
            val paste = withFilter.map {
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
            return StartsUiState.Success(listOf(emptyList(), actual, paste, emptyList()))
        }

        override fun mapAll(vararg items: List<Row>) : StartsUiState {
            val resultLists = items.map { list ->
                list.map {
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
            }
            return StartsUiState.Success(resultLists)
        }
    }
}