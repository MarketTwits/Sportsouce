package com.markettwits.sportsouce.starts.recent.data

import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageListCache
import com.markettwits.cahce.store_wrapper.listStoreOfWrapper
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

internal class StartsRecentCache : InStorageListCache<StartsListItem>(
    listStoreOfWrapper(
        path = InStorageCacheDirectory.path,
        fileName = FILE_NAME,
    )
) {
    override suspend fun set(key: Any, value: StartsListItem) {
        update { current ->
            (listOf(value) + (current ?: emptyList()).filterNot { it.id == value.id })
                .take(MAX_RECENT_STARTS)
        }
    }

    companion object {
        private const val FILE_NAME = "sportsouce.starts.recent"
        private const val MAX_RECENT_STARTS = 20
    }
}
