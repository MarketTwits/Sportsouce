package com.markettwits.sportsouce.start.domain

import com.markettwits.sportsouce.start.presentation.result.model.MemberResult
import com.markettwits.sportsouce.start.presentation.start.component.CommentUiState
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

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
}