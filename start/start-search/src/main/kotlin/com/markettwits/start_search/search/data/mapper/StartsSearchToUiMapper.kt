package com.markettwits.start_search.search.data.mapper

import com.markettwits.cloud.model.starts.StartsRemote
import com.markettwits.start_search.search.domain.StartsSearch

interface StartsSearchToUiMapper {
    fun map(searches: List<String>, start: StartsRemote): StartsSearch
    fun map(value: String): Map<String, String>
}