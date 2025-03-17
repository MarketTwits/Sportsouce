package com.markettwits.popular.popular.domain

import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.datetime.Clock
import kotlinx.datetime.toLocalDateTime

internal class RecentStartsFilterBase : RecentStartsFilter {
    override fun lastThreeMonth(): Map<String, String> {
        // Получаем текущую дату с использованием kotlinx.datetime
        val currentDate = Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date

//        // Создаем форматтер для LocalDate с использованием kotlinx.datetime
//        val formatter: DateTimeFormatter<LocalDate> = DateTimeFormat {
//            year()
//            monthNumber()
//            dayOfMonth()
//            separator("-")
//        }
//
//        // Вычисляем даты (минус 2 и 1 месяц)
//        val twoMonthsAgo = currentDate.minus(2, DateTimeUnit.MONTH)
//        val oneMonthAgo = currentDate.minus(1, DateTimeUnit.MONTH)
//
//        // Форматируем даты в строки
//        val fromDate = twoMonthsAgo.format(formatter)
//        val toDate = oneMonthAgo.format(formatter)
//
//        return mapOf("fromDate" to fromDate, "toDate" to toDate)
        return mapOf()
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