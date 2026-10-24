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
    val isQuestion: Boolean = false,
    val isAnswered: Boolean = false,
    val isReview: Boolean = false,
    val averageScore: Double? = null,
)

@Serializable
data class ReplyData(
    val id: Int = 0,
    val isAdminAnswer: Boolean = false,
    val userName: String,
    val userPhoto: String,
    val commentText: String,
    val createdAt: String,
)


internal fun com.markettwits.sportsouce.start.domain.StartItem.Comments.Row.toCommentData() = CommentData(
    userName = "${user.surname} ${user.name}".trim(),
    userPhoto = user.photo.orEmpty(),
    commentText = comment,
    createdAt = createdAt,
    isQuestion = isQuestion,
    isAnswered = isAnswered,
    isReview = isReview,
    averageScore = averageScore,
    replies = replies.map { reply ->
        ReplyData(
            id = reply.id,
            isAdminAnswer = reply.isAdminAnswer,
            userName = "${reply.user.surname} ${reply.user.name}".trim(),
            userPhoto = reply.user.photo.orEmpty(),
            commentText = reply.comment,
            createdAt = reply.createdAt,
        )
    },
)
