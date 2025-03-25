package com.markettwits.sportsouce.starts.common.data.mapper

import com.markettwits.core.time.TimeMapper
import com.markettwits.core.time.TimePattern
import com.markettwits.sportsouce.starts.cloud.model.NetworkKindOfSport
import com.markettwits.sportsouce.starts.cloud.model.NetworkStartRemoteItem
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

internal abstract class StartsCloudToListMapperAbstract(private val timeMapper: TimeMapper) :
    StartsCloudToListMapper {
    override fun mapSingle(items: List<NetworkStartRemoteItem>): List<StartsListItem> {
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

    private fun mapKindOfSports(kindOfSports: List<NetworkKindOfSport>): List<StartsListItem.KindOfSport> =
        kindOfSports.map {
            StartsListItem.KindOfSport(
                id = it.id,
                name = it.name
            )
        }
}