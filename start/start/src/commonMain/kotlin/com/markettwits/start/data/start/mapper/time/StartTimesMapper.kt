package com.markettwits.start.data.start.mapper.time

import com.markettwits.start.domain.StartItem

internal interface StartTimesMapper {

    fun map(
        beginningRegistry: String?,
        endRegistry: String?,
        beginningStart: String,
        endStart: String?,
    ): StartItem.StartTimes

}