package com.markettwits.start.presentation.start

import com.arkivanov.decompose.value.Value
import com.markettwits.cloud.ext_model.DistanceInfo
import com.markettwits.start.presentation.membres.list.StartMembersUi
import com.markettwits.start.presentation.start.store.StartScreenStore
import kotlinx.coroutines.flow.StateFlow

interface StartScreenComponent {
    val start: StateFlow<StartScreenStore.State>
    fun obtainEvent(intent : StartScreenStore.Intent)

}
sealed class CommentUiState{
    data object Success : CommentUiState()
    data object Loading : CommentUiState()
    class Error(val message : String) : CommentUiState()
}
sealed class CommentMode{
    class Reply(val replier : String, val messageId : Int) : CommentMode()
    data object Base : CommentMode()
}