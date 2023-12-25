package com.markettwits.starts.data

import android.util.Log
import com.arkivanov.decompose.value.MutableValue
import com.markettwits.core_ui.base.Fourth
import com.markettwits.starts.StartsUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import ru.alexpanov.core_network.api.SportsouceApi
import kotlin.system.measureTimeMillis

class BaseStartsDataSource(
    private val service: SportsouceApi,
    private val mapper: StartsCloudToUiMapper,
) : StartsDataSource {

    override val starts: MutableValue<StartsUiState> = MutableValue(StartsUiState.Loading)
    override suspend fun starts() {
            try {
                val (actual, paste, preview, main) = coroutineScope {
                    withContext(Dispatchers.IO){
                        val deferredActual = async { service.fetchActualStarts() }
                        val deferredPaste = async { service.fetchPasteStarts() }
                        val deferredPreview = async { service.fetchPreview() }
                        val deferredMain = async { service.fetchStartMain() }
                        Fourth(deferredMain.await(),deferredActual.await(), deferredPaste.await(), deferredPreview.await())
                    }
                }
                starts.value = mapper.mapAll(actual.rows, paste.rows, preview.rows, main.rows)
            } catch (e: Exception) {
                starts.value = mapper.map(e)
            }
    }
}