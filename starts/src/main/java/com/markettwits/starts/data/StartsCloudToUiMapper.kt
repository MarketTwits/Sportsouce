package com.markettwits.starts.data

import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.core_ui.time.TimePattern
import com.markettwits.sportsourcedemo.all.Row
import com.markettwits.starts.presentation.StartsUiState
import com.markettwits.starts_common.domain.StartsListItem


interface StartsCloudToUiMapper {
    fun map(exception: Throwable): StartsUiState
    fun mapAll(vararg items: List<Row>): StartsUiState
    fun map(vararg items: List<Row>): List<List<StartsListItem>>
    fun mapSingle(items: List<Row>): List<StartsListItem>

    class Base(private val timeMapper: TimeMapper) : StartsCloudToUiMapper {

        override fun map(exception: Throwable): StartsUiState {
            return StartsUiState.Failed(exception.message ?: "")
        }

        override fun map(vararg items: List<Row>): List<List<StartsListItem>> {
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
            return listOf(resultLists[0], resultLists[1])
        }

        override fun mapSingle(items: List<Row>): List<StartsListItem> {
            val resultLists = items.map {
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
            return resultLists
        }


        override fun mapAll(vararg items: List<Row>): StartsUiState {
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