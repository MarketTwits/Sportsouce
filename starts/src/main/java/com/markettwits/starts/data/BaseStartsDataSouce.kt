package com.markettwits.starts.data

import android.util.Log
import com.arkivanov.decompose.value.MutableValue
import com.markettwits.starts.StartsUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import ru.alexpanov.core_network.api.SportsouceApi
import kotlin.system.measureTimeMillis

class BaseStartsDataSource(
    private val service: SportsouceApi,
    private val mapper: StartsCloudToUiMapper
) : StartsDataSource {

    override val starts: MutableValue<StartsUiState> = MutableValue(StartsUiState.Loading)
    override suspend fun starts() {
        val executionTime = measureTimeMillis {
            try {
                val (data, withFilter) = coroutineScope {
                    withContext(Dispatchers.IO){
                        val deferredData = async { service.fetchStarts() }
                        val deferredWithFilter = async { service.fetchStartsWithFilter() }
                        Pair(deferredData.await(), deferredWithFilter.await())
                    }
                }
                starts.value = mapper.map(data.rows, withFilter.rows)
            } catch (e: Exception) {
                starts.value = mapper.map(e)
            }
        }
        Log.d("mt05", executionTime.toString())
    }
}