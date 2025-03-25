package com.markettwits.sportsouce.start.data.start.mapper.time

import com.markettwits.sportsouce.start.domain.StartItem

internal interface StartTimesMapper {

    fun map(
        beginningRegistry: String?,
        endRegistry: String?,
        beginningStart: String,
        endStart: String?,
    ): StartItem.StartTimes

}