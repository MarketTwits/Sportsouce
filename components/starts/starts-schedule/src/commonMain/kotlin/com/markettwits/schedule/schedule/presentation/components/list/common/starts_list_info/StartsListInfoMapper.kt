package com.markettwits.schedule.schedule.presentation.components.list.common.starts_list_info

import com.markettwits.starts_common.domain.StartsListItem

interface StartsListInfoMapper {
    fun map(startsListItem: List<StartsListItem>): List<StartsListInfoItem>
}

object StartsListInfoMapperBase : StartsListInfoMapper {
    override fun map(startsListItem: List<StartsListItem>): List<StartsListInfoItem> {
        val startsCount = startsListItem.size
        val kindOfSportsCount = mapKindOfSportsCount(startsListItem)
        val registryOpenStartsCount = mapRegistryOpenStartsCount(startsListItem)
        val registryCloseStartsCount = startsCount - registryOpenStartsCount
        return listOf(
            StartsListInfoItem(
                "Всего\nстартов",
                startsCount.toString()
            ),
            StartsListInfoItem(
                "Видов\nспорта",
                kindOfSportsCount.toString()
            ),
            StartsListInfoItem(
                "Открытых\nрегистраций",
                registryOpenStartsCount.toString()
            ),
            StartsListInfoItem(
                "Закрытых\nрегистраций",
                registryCloseStartsCount.toString()
            ),
        )
    }

    private fun mapKindOfSportsCount(startsListItem: List<StartsListItem>): Int =
        startsListItem.distinctBy { it.kindOfSports.map { it.id } }.size

    private fun mapRegistryOpenStartsCount(startsListItem: List<StartsListItem>): Int =
        startsListItem.count { it.statusCode.id == 3 }

}