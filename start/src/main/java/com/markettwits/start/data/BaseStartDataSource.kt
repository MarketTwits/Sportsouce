package com.markettwits.start.data

import com.markettwits.cloud.model.start.StartRemote
import ru.alexpanov.core_network.api.SportsouceApi

class BaseStartDataSource(private val sportsouceApi: SportsouceApi) : StartDataSource {
    override suspend fun start(startId: Int): StartRemote {
        val data = runCatching {
            sportsouceApi.fetchStart(startId)
        }
        return data.getOrThrow()
    }
}