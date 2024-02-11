package com.markettwits.schedule.schedule.domain

import com.markettwits.starts_common.domain.StartsListItem


data class StartsSchedule(
    val day: String = "",
    val message: String = "",
    val items: List<StartsListItem> = emptyList()
)