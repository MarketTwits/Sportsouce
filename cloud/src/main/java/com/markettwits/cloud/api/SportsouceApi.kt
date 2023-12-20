package ru.alexpanov.core_network.api

import com.markettwits.cloud.model.start.StartRemote
import com.markettwits.cloud.model.start_comments.StartCommentsRemote
import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.cloud.model.starts.StartsRemote

interface SportsouceApi {
    suspend fun fetchStarts() : StartsRemote
    suspend fun fetchStartsWithFilter() : StartsRemote
    suspend fun fetchStart(startId : Int) : StartRemote
    suspend fun fetchStartMember(startId: Int) : List<StartMemberItem>
    suspend fun fetchStartComments(startId: Int) : StartCommentsRemote
}