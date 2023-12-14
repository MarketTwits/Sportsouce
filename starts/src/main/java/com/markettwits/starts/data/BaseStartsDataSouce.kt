package com.markettwits.starts.data

import com.arkivanov.decompose.value.MutableValue
import com.markettwits.starts.StartsUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alexpanov.core_network.api.SportsouceApi

class BaseStartsDataSource(
    private val service : SportsouceApi,
    private val mapper: StartsCloudToUiMapper
) : StartsDataSource {

    override val starts: MutableValue<StartsUiState> = MutableValue(StartsUiState.Loading)

    override suspend fun starts() {
        val data = runCatching {
            service.fetchStarts()
        }
        data.onFailure {
           starts.value = mapper.map(it)
        }.onSuccess {
            starts.value = mapper.map(it.rows)
        }
    }

    override fun startsWithFilter() {
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            val data = runCatching {
                service.fetchStartsWithFilter()
            }
            data.onFailure {
                starts.value = mapper.map(it)
            }.onSuccess {
                starts.value = mapper.map(it.rows)
            }
        }
    }
}