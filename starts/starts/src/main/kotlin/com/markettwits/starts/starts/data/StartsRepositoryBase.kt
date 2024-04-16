package com.markettwits.starts.starts.data

import android.util.Log
import com.arkivanov.decompose.value.MutableValue
import com.markettwits.cahce.execute.list.ExecuteListWithCache
import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.cloud.exception.networkExceptionHandler
import com.markettwits.cloud.model.seasons.StartSeasonsRemote
import com.markettwits.core_ui.items.base.Fourth
import com.markettwits.starts.starts.domain.StartsRepository
import com.markettwits.starts.starts.presentation.component.StartsUiState
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

internal class StartsRepositoryBase(
    private val service: SportsouceApi,
    private val cache: StartsMainCache,
    private val execute: ExecuteListWithCache,
    private val mapper: StartsCloudToUiMapper,
) : StartsRepository {
    override val starts: MutableValue<StartsUiState> = MutableValue(StartsUiState.Loading)
    override suspend fun starts(forced: Boolean) {
        runCatching {
            execute.executeListWithCache(
                forced = forced,
                cache = cache,
                launch = ::launch,
                callback = {
                    starts.value = mapper.mapSuccess(it)
                },
            )
        }.onFailure {
            Log.e("mt05", it.message.toString())
            starts.value = mapper.map(networkExceptionHandler(it))
        }
    }

    private suspend fun launch(): List<List<StartsListItem>> {
        StartSeasonsRemote
        val (actual, paste, preview, main) = coroutineScope {
            withContext(Dispatchers.IO) {
                val deferredActual = async { service.fetchActualStarts() }
                val deferredPaste = async { service.fetchPasteStarts().rows.reversed() }
                val deferredPreview = async { service.fetchPreview() }
                val deferredMain = async { service.fetchStartMain() }
                Fourth(
                    deferredMain.await(),
                    deferredActual.await(),
                    deferredPaste.await(),
                    deferredPreview.await()
                )
            }
        }
        val data =
            mapper.mapAll(actual.rows, paste.rows, preview, main.rows) as StartsUiState.Success
        return data.items
    }
}