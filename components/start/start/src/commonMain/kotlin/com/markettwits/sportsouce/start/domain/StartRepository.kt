package com.markettwits.sportsouce.start.domain

import app.cash.paging.PagingData
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult
import com.markettwits.sportsouce.start.presentation.start.component.CommentUiState
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface StartRepository {

    suspend fun start(startId: String, relaunch: Boolean): Result<StartItem>

    /**
     * Observable state of favorite starts
     */
    val favoritesFlow: StateFlow<List<StartsListItem>>

    /**
     * Refreshes favorites from network
     */
    suspend fun refreshFavorites(): Result<List<StartsListItem>>

    suspend fun startAddToFavorite(startItem: StartItem): Result<Boolean>

    suspend fun startRemoveFromFavorites(startItem: StartItem): Result<Boolean>

    suspend fun isStartInFavorite(startId: String): Result<Boolean>

    suspend fun startMembersResult(startId: Int, maxResultCount: Int): List<MemberResult>

    suspend fun startComments(startId: Int): Result<StartItem.Comments>

    suspend fun startsRecommended(startId: String): Result<List<StartsListItem>>

    suspend fun startsSeries(seriesId: Int): Result<List<StartsListItem>>

    suspend fun writeComment(startId: Int, comment: String, id: Int, subComment: Boolean): CommentUiState

    suspend fun membersFilters(startId: Int): Result<FiltersRemote>

    fun pagingMembers(
        startId: Int,
        params: StartMembersPagingParams,
    ): Flow<PagingData<Pair<StartMembersUi, Int>>>

    fun pagingMembersResults(
        startId: Int,
        params: StartMembersResultsPagingParams = StartMembersResultsPagingParams(),
    ): Flow<PagingData<Pair<MemberResult, Int>>>

}

