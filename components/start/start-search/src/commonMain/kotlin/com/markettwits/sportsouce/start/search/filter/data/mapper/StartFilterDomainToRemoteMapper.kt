package com.markettwits.sportsouce.start.search.filter.data.mapper

import com.markettwits.sportsouce.start.search.filter.domain.StartFilter
import com.markettwits.sportsouce.start.search.filter.presentation.component.StartFilterUi
import com.markettwits.sportsouce.start.search.filter.presentation.component.StartFilterUi.Key

interface StartFilterDomainToRemoteMapper {
    fun map(
        filter: StartFilterUi,
        value: String,
        sorted: StartFilter.Sorted = StartFilter.Sorted.Popular,
    ): Map<String, String>

    class Base : StartFilterDomainToRemoteMapper {
        override fun map(
            filter: StartFilterUi,
            value: String,
            sorted: StartFilter.Sorted,
        ): Map<String, String> = buildMap {
            fun selected(key: Key) = filter.item(key)?.selected.orEmpty()
            fun addSelection(key: Key, parameter: String) {
                selected(key).takeIf { it.isNotEmpty() }?.let { put(parameter, it.joinToString(",")) }
            }

            if (value.isNotBlank()) put("filterText", value)
            put("group", "false")
            put("maxResultCount", "100")
            put("sorting", when (sorted) {
                StartFilter.Sorted.FirstBefore -> "start_date ASC"
                StartFilter.Sorted.LastBefore -> "start_date DESC"
                StartFilter.Sorted.Popular -> "viewsCount DESC"
            })
            addSelection(Key.Sport, "kindOfSports")
            addSelection(Key.Season, "name")
            addSelection(Key.City, "city")
            selected(Key.Organizer).firstOrNull()?.let { put("organizerName", it) }
            selected(Key.Status).takeIf { it.isNotEmpty() }?.let { put("status", mapStartStatus(it)) }
            if (!filter.hasInvalidPeriod) {
                filter.fromDate?.let { put("fromDate", it.toString()) }
                filter.toDate?.let { put("toDate", it.toString()) }
            }
        }

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
