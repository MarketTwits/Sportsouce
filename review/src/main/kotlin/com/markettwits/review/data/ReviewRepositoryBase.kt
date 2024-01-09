package com.markettwits.review.data

import com.markettwits.starts.StartsListItem
import com.markettwits.starts.data.StartsCloudToUiMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import com.markettwits.cloud.api.SportsouceApi

class ReviewRepositoryBase(
    private val service : SportsouceApi,
    private val mapper : StartsCloudToUiMapper,
) : ReviewRepository {
    override suspend fun launch(): Result<List<List<StartsListItem>>> {
        return runCatching {
            val (actual,archive)  = coroutineScope {
                withContext(Dispatchers.IO){
                    val deferredActual = async { service.fetchActualStarts() }
                    val deferredPaste = async { service.fetchPasteStarts() }
                    Pair(deferredActual.await(),deferredPaste.await())
                }
            }
            mapper.map(actual.rows, archive.rows)
        }
    }
}