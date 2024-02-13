package com.markettwits.starts_common.data

import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.core_ui.time.TimePattern
import com.markettwits.sportsourcedemo.all.Row
import com.markettwits.starts_common.domain.StartsListItem

abstract class StartsCloudToListMapperAbstract(private val timeMapper: TimeMapper) :
    StartsCloudToListMapper {
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
}