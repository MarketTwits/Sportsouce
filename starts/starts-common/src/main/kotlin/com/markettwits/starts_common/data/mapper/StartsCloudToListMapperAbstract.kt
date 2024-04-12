package com.markettwits.starts_common.data.mapper

import com.markettwits.core_ui.time.TimeMapper
import com.markettwits.core_ui.time.TimePattern
import com.markettwits.sportsourcedemo.all.Row
import com.markettwits.starts_common.domain.StartsListItem

abstract class StartsCloudToListMapperAbstract(private val timeMapper: TimeMapper) :
    StartsCloudToListMapper {
    override fun mapSingle(items: List<Row>): List<StartsListItem> {
        val resultLists = items.map {
            StartsListItem(
                id = it.id,
                name = it.name,
                image = it.posterLinkFile?.fullPath ?: "",
                date = timeMapper.mapTime(TimePattern.ddMMMMyyyy, it.start_date),
                statusCode = StartsListItem.StatusCode(
                    it.start_status.code,
                    it.start_status.name
                ),
                place = it.coordinates ?: "",
                distance = it.condition_short ?: "",
                kindOfSports = mapKindOfSports(it.kindOfSports),
                onMainPage = it.on_main_page ?: false,
                views = it.viewsCount
            )
        }
        return resultLists
    }

    private fun mapKindOfSports(kindOfSports: List<com.markettwits.cloud.model.common.KindOfSport>): List<StartsListItem.KindOfSport> =
        kindOfSports.map {
            StartsListItem.KindOfSport(
                id = it.id,
                name = it.name
            )
        }
}