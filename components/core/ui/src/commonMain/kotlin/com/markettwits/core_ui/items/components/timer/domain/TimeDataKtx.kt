package com.markettwits.core_ui.items.components.timer.domain

import com.markettwits.core_ui.items.components.timer.domain.model.TimeData
import com.markettwits.core_ui.items.components.timer.domain.model.TimeUnit

fun parseTimeData(timeString: String): TimeData {
    return try {
        val parts = timeString.split(":")
        if (parts.size == 3) {
            val hours = parseTimeUnit(parts[0])
            val mins = parseTimeUnit(parts[1])
            val secs = parseTimeUnit(parts[2])
            TimeData(hours, mins, secs)
        } else {
            TimeData()
        }
    } catch (e: Exception) {
        TimeData()
    }
}
private fun parseTimeUnit(unit: String): TimeUnit {
    return if (unit.length == 2 && unit.all { it.isDigit() }) {
        TimeUnit(
            leftDigit = unit[0].digitToInt(),
            rightDigit = unit[1].digitToInt()
        )
    } else {
        TimeUnit()
    }
}
