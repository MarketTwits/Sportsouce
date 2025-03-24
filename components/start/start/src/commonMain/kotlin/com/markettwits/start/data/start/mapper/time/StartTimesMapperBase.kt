package com.markettwits.start.data.start.mapper.time

import com.markettwits.start.domain.StartItem
import com.markettwits.time.TimeMapper
import com.markettwits.time.TimePattern

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