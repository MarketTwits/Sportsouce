package com.markettwits.schedule.schedule.presentation.components.list.common.kind_of_sport_list_info

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Sailing
import androidx.compose.material.icons.filled.Snowboarding
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.starts_common.domain.StartsListItem

interface KindOfSportsInfoMapper {

    fun mapStarts(
        startsListItem: List<StartsListItem>,
        defaultColor: Color,
    ): List<KindOfSportsInfo>

    fun mapSelectedSport(
        startsList: List<StartsListItem>, sportId: Int
    ): List<StartsListItem>

    fun mapSportColor(sportId: Int, defaultColor: Color): Color
    fun mapSportIcon(sportId: Int): ImageVector
}

object KindOfSportsInfoMapperBase : KindOfSportsInfoMapper {

    override fun mapStarts(
        startsListItem: List<StartsListItem>,
        defaultColor: Color,
    ): List<KindOfSportsInfo> {
        val kindOfSportsMap = startsListItem
            .flatMap { it.kindOfSports }
            .groupingBy { it.id }
            .eachCount()

        return kindOfSportsMap.map { (id, count) ->
            KindOfSportsInfo(
                sportId = id,
                title = startsListItem.firstOrNull { it.kindOfSports.any { kind -> kind.id == id } }?.kindOfSports
                    ?.first { kind -> kind.id == id }?.name ?: "",
                count = count,
                color = mapSportColor(id, defaultColor),
                icon = mapSportIcon(id)
            )
        }
    }

    override fun mapSelectedSport(
        startsList: List<StartsListItem>,
        sportId: Int
    ): List<StartsListItem> =
        startsList.filter { startsItem ->
            startsItem.kindOfSports.any { kindOfSport -> kindOfSport.id == sportId }
        }

    override fun mapSportColor(sportId: Int, defaultColor: Color): Color =
        sportColors[sportId] ?: defaultColor

    override fun mapSportIcon(sportId: Int): ImageVector =
        sportIcons[sportId] ?: Icons.Default.CalendarMonth

    private val sportColors = mapOf(
        16 to SportSouceColor.SportSouceRegistryOpenGreen,        // bike
        15 to SportSouceColor.SportSouceStartEndedPink,         // triathlon
        14 to SportSouceColor.SportSouceBlue,       // swimming
        9 to SportSouceColor.SportSouceRegistryCommingSoonYellow,       // run
        7 to SportSouceColor.SportSouceLightRed,      // race with obstacles
        2 to SportSouceColor.SportSouceLighBlue          // ski race
    )
    private val sportIcons = mapOf(
        16 to Icons.AutoMirrored.Filled.DirectionsBike,
        15 to Icons.AutoMirrored.Filled.DirectionsRun,
        14 to Icons.Filled.Sailing,
        9 to Icons.AutoMirrored.Filled.DirectionsRun,
        7 to Icons.AutoMirrored.Filled.DirectionsRun,
        2 to Icons.Filled.Snowboarding
    )
}