package com.markettwits.review.data

import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.starts_common.data.StartsCloudToListMapper
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class ReviewRepositoryBase(
    private val service : SportsouceApi,
    private val mapper: StartsCloudToListMapper,
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
            val actualList = mapper.mapSingle(actual.rows)
            val archiveList = mapper.mapSingle(archive.rows)
            listOf(actualList, archiveList)
        }
    }
}