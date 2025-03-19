package com.markettwits.profile.authorized.presentation.components.statistics

import androidx.compose.ui.graphics.Color
import com.markettwits.registrations.list.domain.StartOrderInfo
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

internal fun userStatisticMapper(starts: List<StartOrderInfo>): List<BarchartInput> {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val startDate = LocalDate(currentDate.year - 2, currentDate.month, 1)
    val endDate = LocalDate(currentDate.year, currentDate.month, 1)
    val startCounts = mutableMapOf<String, Int>()
    var tempDate = startDate
    while (tempDate <= endDate) {
        val key = "${tempDate.monthNumber.toString().padStart(2, '0')}.${tempDate.year % 100}"
        startCounts[key] = 0
        tempDate = tempDate.plus(DatePeriod(months = 1))
    }
    for (start in starts) {
        try {
            val date = Instant.parse(start.dateStartCloud).toLocalDateTime(TimeZone.UTC).date
            if (date in startDate..endDate) {
                val key = "${date.monthNumber.toString().padStart(2, '0')}.${date.year % 100}"
                startCounts[key] = startCounts[key]?.plus(1) ?: 1
            }
        } catch (e: Exception) {
            println("Error parsing date: ${start.dateStartPreview}")
        }
    }
    return startCounts.map { (key, value) ->
        BarchartInput(
            value = value,
            description = key,
            color = Color.White // Устанавливаем нужный цвет
        )
    }
}