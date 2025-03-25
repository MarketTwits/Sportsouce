package com.markettwits.sportsouce.start.search.search.domain

import com.markettwits.sportsouce.starts.common.domain.StartsListItem

data class StartsSearch(
    val searches: List<String>,
    val items: List<StartsListItem>
)