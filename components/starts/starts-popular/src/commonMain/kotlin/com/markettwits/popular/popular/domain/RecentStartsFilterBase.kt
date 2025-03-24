package com.markettwits.popular.popular.domain

import com.markettwits.starts_common.domain.StartsListItem
import com.markettwits.time.TimeMapper
import com.markettwits.time.TimePattern
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

internal class RecentStartsFilterBase(
    private val timeMapper: TimeMapper
) : RecentStartsFilter {
    override fun lastThreeMonth(): Map<String, String> {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        val twoMonthsAgo = currentDate.minus(DatePeriod(months = 2))
        val oneMonthAgo = currentDate.minus(DatePeriod(months = 1))

        val fromDate = timeMapper.mapTime(TimePattern.FullWithDots, twoMonthsAgo.toString())
        val toDate = timeMapper.mapTime(TimePattern.FullWithDots, oneMonthAgo.toString())

        return mapOf("fromDate" to fromDate, "toDate" to toDate)
    }

    override suspend fun sortPopularStarts(starts: List<StartsListItem>): List<StartsListItem> =
        starts.sortedBy {
            it.views
        }.reversed()

    companion object {
        private const val LANGUAGE_RU_TAG = "ru"
        private const val DATE_PATTERN = "yyyy-MM-dd" // Пример паттерна
    }
}