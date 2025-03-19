package com.markettwits.start_search.search.data.mapper

import com.markettwits.start_search.search.domain.StartsSearch
import com.markettwits.starts_common.domain.StartsListItem

interface StartsSearchToUiMapper {
    fun map(searches: List<String>, start: List<StartsListItem>): StartsSearch
    fun map(value: String): Map<String, String>
}