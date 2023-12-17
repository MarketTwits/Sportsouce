package com.markettwits.start.data

import com.markettwits.start.presentation.membres.StartMembersUi
import com.markettwits.start.presentation.start.StartItemUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import ru.alexpanov.core_network.api.SportsouceApi

class BaseStartDataSource(
    private val service: SportsouceApi,
    private val mapper: StartRemoteToUiMapper
) : StartDataSource {
    override suspend fun start(startId: Int): StartItemUi {
        val (data, withFilter) = coroutineScope {
            withContext(Dispatchers.IO) {
                val deferredData = async { service.fetchStart(startId) }
                val deferredWithFilter = async { service.fetchStartMember(startId) }
                Pair(deferredData.await(), deferredWithFilter.await())
            }
        }
        return mapper.map(data, withFilter)
    }

    override suspend fun startMember(startId: Int): List<StartMembersUi> {
        val startMember = service.fetchStartMember(startId)
        return mapper.map(startMember)
    }
}