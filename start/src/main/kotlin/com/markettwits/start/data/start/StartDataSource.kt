package com.markettwits.start.data.start

import com.markettwits.start.presentation.membres.list.StartMembersUi
import com.markettwits.start.presentation.start.CommentUiState
import com.markettwits.start.presentation.start.StartItemUi

interface StartDataSource {
    suspend fun start(startId: Int, relaunch: Boolean): StartItemUi
    suspend fun startMember(startId: Int): List<StartMembersUi>
    suspend fun writeComment(comment: String, startId: Int): CommentUiState
    suspend fun writeSubComment(comment: String, parentCommentId: Int): CommentUiState
}