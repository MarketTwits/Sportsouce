package com.markettwits.start.data.start

import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.start.component.CommentUiState

interface StartRepository {
    suspend fun start(startId: Int, relaunch: Boolean): Result<StartItem>
    suspend fun startComments(startId: Int): Result<StartItem.Comments>
    suspend fun writeComment(comment: String, id: Int, subComment: Boolean): CommentUiState

}