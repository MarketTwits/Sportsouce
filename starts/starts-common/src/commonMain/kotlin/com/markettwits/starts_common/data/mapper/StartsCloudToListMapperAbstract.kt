package com.markettwits.starts_common.data.mapper

import com.markettwits.cloud.model.starts.StartRemoteItem
import com.markettwits.starts_common.domain.StartsListItem
import com.markettwits.time.TimeMapper
import com.markettwits.time.TimePattern

abstract class StartsCloudToListMapperAbstract(private val timeMapper: TimeMapper) :
    StartsCloudToListMapper {
    override fun mapSingle(items: List<StartRemoteItem>): List<StartsListItem> {
        val resultLists = items.map {
            StartsListItem(
                id = it.id,
                name = it.name,
                image = it.posterLinkFile?.fullPath ?: "",
                date = timeMapper.mapTime(TimePattern.FullWithEmptySpace, it.start_date),
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