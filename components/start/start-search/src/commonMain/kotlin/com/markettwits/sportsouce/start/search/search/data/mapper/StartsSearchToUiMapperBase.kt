package com.markettwits.sportsouce.start.search.search.data.mapper

import com.markettwits.sportsouce.start.search.search.domain.StartsSearch
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

class StartsSearchToUiMapperBase() : StartsSearchToUiMapper {
    override fun map(searches: List<String>, start: List<StartsListItem>): StartsSearch = StartsSearch(searches, start)

    override fun map(value: String): Map<String, String> =
        mapOf("filterText" to value)
}