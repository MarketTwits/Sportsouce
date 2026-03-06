package com.markettwits.sportsouce.start.domain.mapper

import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.core.time.TimePattern
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

/**
 * Maps StartItem to StartsListItem for favorites operations
 */
object StartItemToStartsListItemMapper {

    private val timeMapper = BaseTimeMapper()

    fun map(startItem: StartItem): StartsListItem {
        return StartsListItem(
            id = startItem.id,
            name = startItem.title,
            image = startItem.image,
            date = runCatching {
                timeMapper.mapTime(TimePattern.FullWithEmptySpace, startItem.startData)
            }.getOrDefault(startItem.startData),
            statusCode = StartsListItem.StatusCode(
                id = startItem.startStatus.code,
                message = startItem.startStatus.name
            ),
            description = startItem.description,
            slug = startItem.slug,
            place = startItem.startPlace,
            onMainPage = false, // Not available in StartItem
            distance = "", // Can be extracted from distanceInfoNew if needed
            kindOfSports = startItem.kindOfSports.map { kindOfSport ->
                StartsListItem.KindOfSport(
                    id = kindOfSport.id,
                    name = kindOfSport.name
                )
            },
            views = 0 // Not available in StartItem
        )
    }
}
