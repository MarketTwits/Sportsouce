package ru.alexpanov.core_network.api

import com.markettwits.cloud.model.start.StartRemote
import com.markettwits.cloud.model.start_member.StartMemberItem

interface SportsouceApi {
    suspend fun fetchStarts() : ru.alexpanov.core_network.model.all.StartsRemote
    suspend fun fetchStartsWithFilter() : ru.alexpanov.core_network.model.all.StartsRemote
    suspend fun fetchStart(startId : Int) : StartRemote
    suspend fun fetchStartMember(startId: Int) : List<StartMemberItem>
}