package com.markettwits.start.data

import com.markettwits.cloud.model.start_comments.StartCommentsRemote
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
        val (data, withFilter, comments) = coroutineScope {
            withContext(Dispatchers.IO) {
                val deferredData = async { service.fetchStart(startId) }
                val deferredWithFilter = async { service.fetchStartMember(startId) }
                val deferredComments = async { service.fetchStartComments(startId) }
                Triple(deferredData.await(), deferredWithFilter.await(), deferredComments.await())
            }
        }
        return mapper.map(data, withFilter, comments)
    }

    override suspend fun startMember(startId: Int): List<StartMembersUi> {
        val startMember = service.fetchStartMember(startId)
        return mapper.map(startMember)
    }

}