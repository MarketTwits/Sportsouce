package com.markettwits.popular.popular.domain

import com.markettwits.starts_common.domain.StartsListItem

internal interface RecentStartsFilter {
    fun lastThreeMonth(): Map<String, String>
    suspend fun sortPopularStarts(starts: List<StartsListItem>): List<StartsListItem>
}