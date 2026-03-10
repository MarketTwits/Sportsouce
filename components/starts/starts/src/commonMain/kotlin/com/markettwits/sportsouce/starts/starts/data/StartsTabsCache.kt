package com.markettwits.sportsouce.starts.starts.data

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageSingleCache
import com.markettwits.cahce.store_wrapper.storeOfWrapper
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.serialization.Serializable

private const val FILE_NAME = "sportsouce.starts.tabs"

@Serializable
internal data class StartsTabsCacheModel(
    val main: List<StartsListItem> = emptyList(),
    val actual: List<StartsListItem> = emptyList(),
    val past: List<StartsListItem> = emptyList(),
    val preview: List<StartsListItem> = emptyList(),
)

internal class StartsTabsCache : InStorageSingleCache<StartsTabsCacheModel>(
    storeOfWrapper(
        path = InStorageCacheDirectory.path,
        fileName = FILE_NAME
    )
)
