package com.markettwits.start_search.filter.data.mapper

import com.markettwits.start_search.filter.domain.StartFilter
import com.markettwits.start_search.filter.presentation.component.StartFilterUi
import com.markettwits.starts_common.domain.StartsListItem

interface StartFilterDomainToRemoteMapper {
    fun map(startItemUi: StartFilterUi, value: String): Map<String, String>
    fun sort(sorted: StartFilter.Sorted, starts: List<StartsListItem>): List<StartsListItem>
    class Base : StartFilterDomainToRemoteMapper {
        override fun map(startItemUi: StartFilterUi, value: String): Map<String, String> {
            val map = mutableMapOf<String, String>()
            val item = startItemUi.items

            if (value.isNotBlank()) {
                map["filterText"] = value
            }

            map["group"] = "false"
            map["maxResultCount"] = "100"

            if (item.isNotEmpty() && item[0].selected.isNotEmpty()) {
                map["kindOfSports"] = mapSelectedItems(item[0].selected)
            }

            if (item.size > 1 && item[1].selected.isNotEmpty()) {
                map["name"] = mapSelectedItems(item[1].selected)
            }

            if (item.size > 2 && item[2].selected.isNotEmpty()) {
                map["city"] = mapSelectedItems(item[2].selected)
            }

            if (item.size > 4 && item[4].selected.isNotEmpty()) {
                map["status"] = mapStartStatus(item[4].selected)
            }

            return map
        }

        override fun sort(
            sorted: StartFilter.Sorted,
            starts: List<StartsListItem>
        ): List<StartsListItem> {
            return when (sorted) {
                is StartFilter.Sorted.FirstBefore -> starts.sortedBy { it.date }
                is StartFilter.Sorted.LastBefore -> starts.sortedByDescending { it.date }
                is StartFilter.Sorted.Popular -> starts.sortedByDescending { it.views }
            }
        }

        private fun mapSelectedItems(items: List<String>) = items.joinToString(",")
        private fun mapStartStatus(items: List<String>): String {
            val itemSet = items.toSet()
            return when {
                "Прошедшие" in itemSet -> "0,6"
                "Актуальные" in itemSet -> "1,2,3,4,5"
                else -> "0,1,2,3,4,5,6"
            }
        }
    }
}

fun StartFilterUi.toMap(): Map<String, String> {
    val resultMap = mutableMapOf<String, String>()

    for (item in items) {
        for (selectedItem in item.selected) {
            if (selectedItem in item.list) {
                resultMap[item.label] = selectedItem
            }
        }
    }
    return resultMap
}

