package com.markettwits.start_search.search.domain

import com.markettwits.starts_common.domain.StartsListItem

data class StartsSearch(
    val searches: List<String>,
    val items: List<StartsListItem>
)