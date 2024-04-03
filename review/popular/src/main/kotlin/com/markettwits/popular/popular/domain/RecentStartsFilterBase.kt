package com.markettwits.popular.popular.domain

import com.markettwits.starts_common.domain.StartsListItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal class RecentStartsFilterBase : RecentStartsFilter {
    override fun lastThreeMonth(): Map<String, String> {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val twoMonthsAgo = currentDate.minusMonths(2)
        val oneMonthAgo = currentDate.minusMonths(1)
        val fromDate = twoMonthsAgo.format(formatter)
        val toDate = oneMonthAgo.format(formatter)
        return mapOf("fromDate" to fromDate, "toDate" to toDate)
    }

    override suspend fun sortPopularStarts(starts: List<StartsListItem>): List<StartsListItem> =
        starts.sortedBy {
            it.views
        }.reversed()
}