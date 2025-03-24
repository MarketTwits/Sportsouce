package com.markettwits.core_ui.items.components.timer.domain.model

data class TimeData(
    val hours: TimeUnit = TimeUnit(),
    val mins: TimeUnit = TimeUnit(),
    val secs: TimeUnit = TimeUnit(),
) {
    fun isDataFull() = hours.leftDigit > 0

    fun isDataEmpty() =
        hours.leftDigit == 0 && hours.rightDigit == 0
                && mins.leftDigit == 0 && mins.rightDigit == 0
                && secs.leftDigit == 0 && secs.rightDigit == 0

    fun isHoursHalfFull() = hours.leftDigit == 0 && hours.rightDigit > 0

    override fun toString(): String = "$hours:$mins:$secs"

}