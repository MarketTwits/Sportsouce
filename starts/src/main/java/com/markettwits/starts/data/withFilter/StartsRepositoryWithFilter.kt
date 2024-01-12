package com.markettwits.starts.data.withFilter

import com.arkivanov.decompose.value.MutableValue
import com.markettwits.cloud.api.SportsouceApi
import com.markettwits.core_ui.base.Fourth
import com.markettwits.starts.data.StartsCloudToUiMapper
import com.markettwits.starts.data.StartsRepository
import com.markettwits.starts.presentation.StartsUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class StartsRepositoryWithFilter(
    private val service: SportsouceApi,
    private val mapper: StartsCloudToUiMapper,
) : StartsRepository {

    override val starts: MutableValue<StartsUiState> = MutableValue(StartsUiState.Loading)
    override suspend fun startsFilter(request: Map<String, String>) {
        try {
            val withFilter = coroutineScope {
                withContext(Dispatchers.IO) {
                    async { service.startWithFilter(request) }.await()
                }
            }
            starts.value = mapper.mapAll(withFilter.rows)
        } catch (e: Exception) {
            starts.value = mapper.map(e)
        }
    }
}