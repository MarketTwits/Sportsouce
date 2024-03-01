package com.markettwits.start_search.search.data.local


import com.markettwits.cahce.InStorageCache
import com.markettwits.cahce.InStorageListCache
import com.markettwits.cahce.store.listStoreOfWrapper
import com.markettwits.start_search.search.domain.SearchHistory

class SearchHistoryCache : InStorageListCache<SearchHistory>(
    listStoreOfWrapper<SearchHistory>(
        path = InStorageCache.path,
        fileName = "searchHistory",
    )
) {
    suspend fun add(value: String) {
        val list = getList()
        set(value = SearchHistory(value))
        if (list.size > 20) {
            remove(value = list.last())
        }
    }

    override suspend fun getList(key: Any): List<SearchHistory> =
        super.getList(key).reversed()
}