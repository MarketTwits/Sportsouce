package com.markettwits.sportsouce.start.search.search.data.mapper

import com.markettwits.sportsouce.start.search.search.domain.StartsSearch
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

interface StartsSearchToUiMapper {
    fun map(searches: List<String>, start: List<StartsListItem>): StartsSearch
    fun map(value: String): Map<String, String>
}