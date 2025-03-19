package com.markettwits.start_search.search.data.mapper

import com.markettwits.start_search.search.domain.StartsSearch
import com.markettwits.starts_common.domain.StartsListItem

class StartsSearchToUiMapperBase() : StartsSearchToUiMapper {
    override fun map(searches: List<String>, start: List<StartsListItem>): StartsSearch = StartsSearch(searches, start)

    override fun map(value: String): Map<String, String> =
        mapOf("filterText" to value)
}