package com.markettwits.starts.starts.data

import com.arkivanov.decompose.value.MutableValue
import com.markettwits.cahce.execute.list.ExecuteListWithCache
import com.markettwits.core.errors.api.throwable.networkExceptionHandler
import com.markettwits.core_ui.items.extensions.Fourth
import com.markettwits.starts.starts.domain.StartsRepository
import com.markettwits.starts.starts.presentation.component.StartsUiState
import com.markettwits.starts_common.domain.SportSauceStartsApi
import com.markettwits.starts_common.domain.StartsListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

internal class StartsRepositoryBase(
    private val service: SportSauceStartsApi,
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
            starts.value = mapper.map(it.networkExceptionHandler())
        }
    }

    private suspend fun launch(): List<List<StartsListItem>> {
        val (actual, paste, preview, main) = coroutineScope {
            withContext(Dispatchers.Main.immediate) {
                val deferredActual = async { service.fetchActualStarts() }
                val deferredPaste = async { service.fetchPasteStarts().reversed() }
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
        val data = mapper.mapAll(actual, paste, preview, main) as StartsUiState.Success
        return data.items
    }
}