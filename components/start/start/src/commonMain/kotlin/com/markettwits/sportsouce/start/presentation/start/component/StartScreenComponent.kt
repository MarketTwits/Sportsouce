package com.markettwits.sportsouce.start.presentation.start.component

import com.markettwits.sportsouce.start.presentation.start.store.StartScreenStore
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

interface StartScreenComponent {

    val start: StateFlow<StartScreenStore.State>

    fun obtainEvent(intent: StartScreenStore.Intent)
}

sealed class StartFavoriteState(val isFavorite: Boolean) {
    class Loading(val value: Boolean = false) : StartFavoriteState(value)
    class Default(value: Boolean) : StartFavoriteState(value)

    fun isLoading(): Boolean = this is Loading
}

sealed class CommentUiState {

    data object Success : CommentUiState()

    data object Loading : CommentUiState()

    class Error(val message: String) : CommentUiState()
}

@Serializable
sealed class CommentMode {

    @Serializable
    data class Reply(val replier: String, val messageId: Int, val parentComment: CommentData) : CommentMode()

    @Serializable
    data object Base : CommentMode()
}

@Serializable
data class CommentData(
    val userName: String,
    val userPhoto: String,
    val commentText: String,
    val createdAt: String,
    val replies: List<ReplyData>,
)

@Serializable
data class ReplyData(
    val userName: String,
    val userPhoto: String,
    val commentText: String,
    val createdAt: String,
)

