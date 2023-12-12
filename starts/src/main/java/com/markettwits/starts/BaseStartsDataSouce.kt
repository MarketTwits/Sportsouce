package com.markettwits.starts

import com.arkivanov.decompose.value.MutableValue
import com.markettwits.starts.data.StartsCloudToUiMapper
import com.markettwits.starts.data.StartsDataSource
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
}