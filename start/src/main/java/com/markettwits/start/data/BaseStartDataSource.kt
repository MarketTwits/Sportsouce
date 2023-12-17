package com.markettwits.start.data

import com.markettwits.cloud.model.start_member.StartMember
import com.markettwits.start.presentation.membres.StartMembersUi
import com.markettwits.start.presentation.start.StartItemUi
import ru.alexpanov.core_network.api.SportsouceApi

class BaseStartDataSource(
    private val sportsouceApi: SportsouceApi,
    private val mapper: StartRemoteToUiMapper
) : StartDataSource {
    override suspend fun start(startId: Int): StartItemUi {
        val data = sportsouceApi.fetchStart(startId)
        return mapper.map(data)
    }

    override suspend fun startMember(startId: Int): List<StartMembersUi> {
        val startMember = sportsouceApi.fetchStartMember(startId)
        return mapper.map(startMember)
    }
}