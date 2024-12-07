package com.markettwits.cloud.api

import com.markettwits.cloud.model.time.TimeRemote

interface TimeApi {

    suspend fun currentTime() : TimeRemote

}