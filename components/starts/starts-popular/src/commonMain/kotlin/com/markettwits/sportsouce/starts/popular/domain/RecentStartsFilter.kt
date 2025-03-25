package com.markettwits.sportsouce.starts.popular.domain

import com.markettwits.sportsouce.starts.common.domain.StartsListItem

internal interface RecentStartsFilter {
    fun lastThreeMonth(): Map<String, String>
    suspend fun sortPopularStarts(starts: List<StartsListItem>): List<StartsListItem>
}