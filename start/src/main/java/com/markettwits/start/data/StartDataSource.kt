package com.markettwits.start.data

import com.markettwits.cloud.model.start.StartRemote

interface StartDataSource {
    suspend fun start(startId : Int) : StartRemote
}