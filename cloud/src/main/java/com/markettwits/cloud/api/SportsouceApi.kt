package ru.alexpanov.core_network.api

import com.markettwits.cloud.model.start.StartRemote

interface SportsouceApi {
    suspend fun fetchStarts() : ru.alexpanov.core_network.model.all.StartsRemote
    suspend fun fetchStartsWithFilter() : ru.alexpanov.core_network.model.all.StartsRemote
    suspend fun fetchStart(startId : Int) : StartRemote
}