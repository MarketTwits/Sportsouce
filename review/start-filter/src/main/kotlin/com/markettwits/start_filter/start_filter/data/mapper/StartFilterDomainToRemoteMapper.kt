package com.markettwits.start_filter.start_filter.data.mapper

import com.markettwits.start_filter.start_filter.presentation.StartFilterUi

internal interface StartFilterDomainToRemoteMapper {
    fun map(startItemUi: StartFilterUi): Map<String, String>
    class Base : StartFilterDomainToRemoteMapper {
        override fun map(startItemUi: StartFilterUi): Map<String, String> {
            val map = mutableMapOf<String, String>()
            val item = startItemUi.items
            map["group"] = "false"
            map["maxResultCount"] = "100"
            map["kindOfSports"] = mapSelectedItems(item[0].selected)
            map["name"] = mapSelectedItems(item[1].selected)
            map["city"] = mapSelectedItems(item[2].selected)
            map["status"] = mapStartStatus(item[4].selected)
            return map
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

