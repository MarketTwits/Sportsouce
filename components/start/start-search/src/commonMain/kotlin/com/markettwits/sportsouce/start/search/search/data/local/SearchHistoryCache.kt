package com.markettwits.sportsouce.start.search.search.data.local


import com.markettwits.cahce.InStorageCacheDirectory
import com.markettwits.cahce.InStorageListCache
import com.markettwits.cahce.store_wrapper.listStoreOfWrapper
import com.markettwits.sportsouce.start.search.search.domain.SearchHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SearchHistoryCache : InStorageListCache<SearchHistory>(
    listStoreOfWrapper<SearchHistory>(
        path = InStorageCacheDirectory.path,
        fileName = "sportsouce.start.search",
    )
) {
    override suspend fun set(key: Any, value: SearchHistory) {
        val list = getList()
        if ((list.firstOrNull() ?: "") != value.value) {
            if (list.contains(value)) {
                remove(value = value)
            }
            super.set(value = value, key = key)
            if (list.size > 20) {
                remove(value = list.last())
            }
        }
    }

    override fun observe(): Flow<List<SearchHistory>?> {
        return super.observe().map { it?.reversed() }
    }

    override suspend fun getList(key: Any): List<SearchHistory> =
        super.getList(key).reversed()
}