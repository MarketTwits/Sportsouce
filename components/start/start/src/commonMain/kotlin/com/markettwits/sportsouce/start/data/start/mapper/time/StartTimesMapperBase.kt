package com.markettwits.sportsouce.start.data.start.mapper.time

import com.markettwits.core.time.TimeMapper
import com.markettwits.core.time.TimePattern
import com.markettwits.sportsouce.start.domain.StartItem
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlin.time.Instant

internal class StartTimesMapperBase(private val timeMapper: TimeMapper) : StartTimesMapper {

    override fun map(
        beginningRegistry: String?,
        endRegistry: String?,
        beginningStart: String,
        endStart: String?,
    ): StartItem.StartTimes =

        StartItem.StartTimes(
            beginningRegistry = "",
            endRegistry = "",
            beginningStart = mapTime(beginningStart),
            endStart = "",
            startsAt = runCatching {
                val value = beginningStart.trim()
                if (value.length == 10) LocalDate.parse(value).atStartOfDayIn(TimeZone.UTC)
                else Instant.parse(value)
            }.getOrNull(),
        )

    private fun mapTime(date: String): String = timeMapper.mapTime(TimePattern.Full, date)
}
