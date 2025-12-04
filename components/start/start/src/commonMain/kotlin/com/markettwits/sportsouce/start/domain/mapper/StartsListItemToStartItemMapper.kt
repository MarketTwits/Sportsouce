package com.markettwits.sportsouce.start.domain.mapper

import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

/**
 * Maps StartsListItem to a partial StartItem for immediate UI display
 * while full data is being loaded in the background
 */
object StartsListItemToStartItemMapper {

    fun mapToPartialStartItem(startsListItem: StartsListItem): StartItem {
        return StartItem(
            id = startsListItem.id,
            title = startsListItem.name,
            startPlace = startsListItem.place,
            slug = "", // Not available in StartsListItem
            image = startsListItem.image,
            startMembersUi = emptyList(), // Not available in StartsListItem
            kindOfSports = startsListItem.kindOfSports.map { kindOfSport ->
                StartItem.KindOfSport(
                    id = kindOfSport.id,
                    name = kindOfSport.name
                )
            },
            startStatus = StartItem.StartStatus(
                code = startsListItem.statusCode.id,
                name = startsListItem.statusCode.message
            ),
            startData = startsListItem.date,
            startAlbum = emptyList(), // Not available in StartsListItem
            startTime = "", // Not available in StartsListItem
            description = startsListItem.description,
            paymentDisabled = false, // Not available in StartsListItem
            regLink = "", // Not available in StartsListItem
            distanceInfoNew = emptyList(), // Not available in StartsListItem
            distanceMapNew = emptyList(), // Not available in StartsListItem
            paymentType = "", // Not available in StartsListItem
            organizers = emptyList(), // Not available in StartsListItem
            membersResults = emptyList(), // Not available in StartsListItem
            conditionFile = StartItem.ConditionFile.Empty, // Not available in StartsListItem
            commentsRemote = StartItem.Comments(
                id = 0,
                rows = emptyList()
            ), // Not available in StartsListItem
            result = emptyList(), // Not available in StartsListItem
            usefulLinks = emptyList(), // Not available in StartsListItem,
            startSeries = StartItem.StartSeries.Empty,
            sponsors = emptyList(),
            regOnSite = false,
            conditionDetails = emptyList(),
            startTimes = StartItem.StartTimes(
                beginningRegistry = "",
                endRegistry = "",
                beginningStart = "",
                endStart = ""
            ) // Not available in StartsListItem
        )
    }
}