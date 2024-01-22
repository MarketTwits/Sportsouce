package com.markettwits.start.data.start

import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.membres.list.StartMembersUi
import com.markettwits.start.presentation.start.CommentUiState

interface StartDataSource {
    suspend fun start(startId: Int, relaunch: Boolean): Result<StartItem>
    suspend fun startComments(startId: Int) : Result<StartItem.Comments>
    suspend fun writeComment(comment: String, startId: Int): CommentUiState
    suspend fun writeSubComment(comment: String, parentCommentId: Int): CommentUiState
}