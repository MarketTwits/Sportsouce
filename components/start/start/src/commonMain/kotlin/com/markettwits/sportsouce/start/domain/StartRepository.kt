package com.markettwits.sportsouce.start.domain

import app.cash.paging.PagingData
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult
import com.markettwits.sportsouce.start.presentation.start.component.CommentUiState
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.coroutines.flow.Flow

interface StartRepository {

    suspend fun start(startId: Int, relaunch: Boolean): Result<StartItem>

    suspend fun startMemberResults(
        startId: Int,
        query: String,
        limit: Int,
        offset: Int,
    ): Result<List<MemberResult>>

    suspend fun startComments(startId: Int): Result<StartItem.Comments>

    suspend fun startsRecommended(startId : Int): Result<List<StartsListItem>>

    suspend fun writeComment(startId: Int, comment: String, id: Int, subComment: Boolean): CommentUiState

    // New API-driven members and filters
    suspend fun membersFilters(startId: Int): Result<FiltersRemote>

    // Paging API for members: repository owns Pager creation
    fun pagingMembers(
        startId: Int,
        params: StartMembersPagingParams,
    ): Flow<PagingData<Pair<StartMembersUi, Int>>>
}

