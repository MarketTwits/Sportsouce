package com.markettwits.start.presentation.start

import com.arkivanov.decompose.value.Value
import com.markettwits.start.presentation.membres.list.StartMembersUi

interface StartScreen {
    val start: Value<StartItemUi>

    val commentsState : Value<CommentUiState>
    val commentMode : Value<CommentMode>
    fun sendComment(value : String,commentParentId : Int = 0)
    fun goBack()
    fun goMembers(membersUi: List<StartMembersUi>)
    fun messageHasBeenShowed()
    fun onClickReply(replier : String, id: Int)
    fun onClickCloseReply()
    fun retry()
    fun onClickDistance()
}
sealed class CommentUiState{
    data object Success : CommentUiState()
    data object Loading : CommentUiState()
    class Error(val message : String, val show : Boolean = true) : CommentUiState()
}
sealed class CommentMode{
    class Reply(val replier : String, val messageId : Int) : CommentMode()
    data object Base : CommentMode()
}