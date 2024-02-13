package com.markettwits.start_filter.start_filter.data.mapper

import com.markettwits.cloud.model.starts.StartsRemote
import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.core_ui.time.TimePattern
import com.markettwits.starts_common.domain.StartsListItem

class StartsCloudToFilterMapperBase(private val timeMapper: TimeMapper) :
    StartsCloudToFilterMapper {
    override fun map(start: StartsRemote): List<StartsListItem> {
        val items = start.rows
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