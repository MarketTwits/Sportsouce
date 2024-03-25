package com.markettwits.schedule.schedule.data.cache

import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.serialization.Serializable

@Serializable
internal data class StartsScheduleCacheContent(
    val items: List<StartsListItem>
)