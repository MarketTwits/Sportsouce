package com.markettwits.profile.authorized.presentation.components.statistics

import androidx.compose.ui.graphics.Color
import com.markettwits.core_ui.time.TimePattern
import com.markettwits.registrations.registrations_list.domain.StartOrderInfo
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal fun userStatisticMapper(starts: List<StartOrderInfo>): List<BarchartInput> {
    val currentDate = LocalDate.now()
    val startDate = currentDate.minusYears(2).withDayOfMonth(1)
    val endDate = currentDate.withDayOfMonth(1)

    val startCounts = mutableMapOf<String, Int>()

    // Initialize startCounts with 0 counts for each month
    var tempDate = startDate
    while (tempDate.isBefore(endDate) || tempDate == endDate) {
        startCounts[tempDate.format(DateTimeFormatter.ofPattern("MM.yy"))] = 0
        tempDate = tempDate.plusMonths(1)
    }

    // Count starts for each month
    val formatter = DateTimeFormatter.ofPattern(TimePattern.Remote.map())
    for (start in starts) {
        try {
            val date = LocalDate.parse(start.dateStartCloud, formatter)
            if (date.isAfter(startDate.minusMonths(1)) && date.isBefore(endDate.plusMonths(1))) {
                val key = date.format(DateTimeFormatter.ofPattern("MM.yy"))
                startCounts[key] = startCounts.getOrDefault(key, 0) + 1
            }
        } catch (e: Exception) {
            println("Error parsing date: ${start.dateStartPreview}")
        }
    }

    // Convert startCounts to BarchartInput objects
    return startCounts.map { entry ->
        BarchartInput(
            value = entry.value,
            description = entry.key,
            color = Color.White // Set your desired color
        )
    }

}