package com.markettwits.sportsouce.start.data.start.mapper.time

import com.markettwits.core.time.TimeMapper
import com.markettwits.core.time.TimePattern
import com.markettwits.sportsouce.start.domain.StartItem

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
            endStart = ""
        )

    private fun mapTime(date: String): String = timeMapper.mapTime(TimePattern.Full, date)
}