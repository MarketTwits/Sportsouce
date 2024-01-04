package com.markettwits.start.presentation.start

import android.util.Log
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.markettwits.start.data.StartDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartScreenInstanceKeeper(
    private val service: StartDataSource,
    private val startId: Int
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(Dispatchers.Main)
    val start: MutableValue<StartItemUi> = MutableValue(StartItemUi.Loading)
    val commentsState: MutableValue<CommentUiState> = MutableValue(CommentUiState.Success)
    val commentMode: MutableValue<CommentMode> = MutableValue(CommentMode.Base)

    init {
        launch()
    }

    fun sendComment(value: String, id: Int) {
        commentsState.value = CommentUiState.Loading
        scope.launch {
            if (commentMode.value is CommentMode.Reply) {
                commentsState.value = service.writeSubComment(
                    value,
                    (commentMode.value as CommentMode.Reply).messageId
                )
            } else {
                commentsState.value = service.writeComment(value, id)
            }
            if (commentsState.value is CommentUiState.Success)
                start.value = service.start(startId)
        }
    }

    fun launch() {
        scope.launch {
            start.value = StartItemUi.Loading
            start.value = service.start(startId)
        }
    }

    fun updateAlertState() {
        commentsState.value = CommentUiState.Error("", false)
    }

    fun onClickReply(replier: String, commentId: Int) {
        commentMode.value = CommentMode.Reply(replier, messageId = commentId)
    }

    fun onClickCloseReply() {
        commentMode.value = CommentMode.Base
    }
}