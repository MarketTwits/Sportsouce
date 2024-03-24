package com.markettwits.schedule.schedule.presentation.components.list.common.kind_of_sport_list_info

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Sailing
import androidx.compose.material.icons.filled.Snowboarding
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.markettwits.starts_common.domain.StartsListItem

interface KindOfSportsInfoMapper {

    fun map(
        startsListItem: List<StartsListItem>,
        defaultColor: Color,
    ): List<KindOfSportsInfo>
}

object KindOfSportsInfoMapperBase : KindOfSportsInfoMapper {

    override fun map(
        startsListItem: List<StartsListItem>,
        defaultColor: Color,
    ): List<KindOfSportsInfo> {
        val kindOfSportsMap = startsListItem
            .flatMap { it.kindOfSports }
            .groupingBy { it.id }
            .eachCount()

        return kindOfSportsMap.map { (id, count) ->
            KindOfSportsInfo(
                startId = id,
                title = startsListItem.firstOrNull { it.kindOfSports.any { kind -> kind.id == id } }?.kindOfSports
                    ?.first { kind -> kind.id == id }?.name ?: "",
                count = count,
                color = mapSportColor(id, defaultColor),
                icon = mapSportIcon(id)
            )
        }
    }

    private fun mapSportColor(sportId: Int, defaultColor: Color): Color =
        sportColors[sportId] ?: defaultColor

    private fun mapSportIcon(sportId: Int): ImageVector =
        sportIcons[sportId] ?: Icons.Default.CalendarMonth

    private val sportColors = mapOf(
        16 to Color.Blue,        // bike
        15 to Color.Red,         // triathlon
        14 to Color.Green,       // swimming
        9 to Color.Yellow,       // run
        7 to Color.Magenta,      // race with obstacles
        2 to Color.Cyan          // ski race
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