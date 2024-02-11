package com.markettwits.start_search.search.data.mapper

import com.markettwits.cloud.model.starts.StartsRemote
import com.markettwits.starts_common.domain.StartsListItem

interface StartsSearchToUiMapper {
    fun map(start: StartsRemote): List<StartsListItem>
    fun map(value: String): Map<String, String>
}