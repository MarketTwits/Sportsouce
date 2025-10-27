package com.markettwits.sportsouce.start.presentation.comments.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.StateEventWithContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.core_ui.items.event.triggered
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.domain.StartRepository
import com.markettwits.sportsouce.start.presentation.comments.store.StartCommentsStore.*
import com.markettwits.sportsouce.start.presentation.comments.store.StartCommentsStoreFactory.Msg.*
import com.markettwits.sportsouce.start.presentation.start.component.CommentMode
import com.markettwits.sportsouce.start.presentation.start.component.CommentMode.Reply
import com.markettwits.sportsouce.start.presentation.start.component.CommentUiState
import kotlinx.coroutines.launch

interface StartCommentsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ApplyStartId(val startId: Int) : Intent
        data class OnClickReply(val replier: String, val commentId: Int) : Intent
        data object OnClickCloseReply : Intent
        data object OnConsumedEvent : Intent
        data class OnClickSendComment(val value: String) : Intent
        data object OnClickGoBack : Intent
        data class OnClickShowReplies(val commentId: Int) : Intent
        data object OnRefresh : Intent
    }

    data class State(
        val startId: Int? = null,
        val comments: StartItem.Comments = StartItem.Comments(0, emptyList()),
        val mode: CommentMode = CommentMode.Base,
        val comment: String = "",
        val isLoadingComments: Boolean = false,
        val isLoadingSendComment: Boolean = false,
        val error: SauceError? = null,
        val event: StateEventWithContent<EventContent> = consumed(),
    )

    sealed interface Label {
        data object OnGoBack : Label
        data class OnNavigateToReplies(val commentId: Int) : Label
    }
}

class StartCommentsStoreFactory(
    private val storeFactory: StoreFactory,
    private val service: StartRepository
) {

    fun create(mode: CommentMode = CommentMode.Base): StartCommentsStore =
        object : StartCommentsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "CommentsStore",
            initialState = State(mode = mode),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { ExecutorImpl() },
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data class UpdateStartId(val startId: Int) : Msg
        data class OnClickReply(val replier: String, val commentId: Int) : Msg
        data object OnClickCloseReply : Msg
        data class OnClickSendComment(val value: String) : Msg
        data class ShowEvent(val success: Boolean, val message: String) : Msg
        data object OnConsumedEvent : Msg
        data object LoadingComments : Msg
        data object LoadingSendComment : Msg
        data class Loaded(val state: StartItem.Comments) : Msg
        data class Error(val error: Throwable) : Msg
    }

    private inner class ExecutorImpl() :
        CoroutineExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.OnClickCloseReply -> dispatch(OnClickCloseReply)
                is Intent.OnClickReply -> dispatch(
                    OnClickReply(
                        intent.replier,
                        intent.commentId
                    )
                )

                is Intent.OnClickSendComment -> state().startId?.let { startId ->
                    sendComment(state().mode, intent.value, startId)
                }

                is Intent.OnConsumedEvent -> dispatch(OnConsumedEvent)
                is Intent.ApplyStartId -> {
                    dispatch(UpdateStartId(intent.startId))
                    launch(intent.startId)
                }

                is Intent.OnClickGoBack -> publish(Label.OnGoBack)
                is Intent.OnClickShowReplies -> publish(Label.OnNavigateToReplies(intent.commentId))
                is Intent.OnRefresh -> state().startId?.let { startId ->
                    launch(startId)
                }
            }
        }

        private fun launch(startId: Int) {
            dispatch(LoadingComments)
            scope.launch {
                service.startComments(startId)
                    .onSuccess {
                        dispatch(Loaded(it))
                    }
                    .onFailure {
                        dispatch(Error(it))
                    }
            }
        }

        private fun sendComment(mode: CommentMode, comment: String, startId: Int) {
            dispatch(LoadingSendComment)
            scope.launch {
                val value = if (mode is Reply) {
                    service.writeComment(
                        comment = comment,
                        startId = startId,
                        id = mode.messageId,
                        subComment = true
                    )

                } else {
                    service.writeComment(
                        comment = comment,
                        id = startId,
                        startId = startId,
                        subComment = false
                    )
                }
                handleCommentUiState(value, startId)
            }
        }

        private fun handleCommentUiState(value: CommentUiState, startId: Int) {
            when (value) {
                is CommentUiState.Error -> {
                    dispatch(ShowEvent(false, value.message))
                }

                is CommentUiState.Loading -> dispatch(LoadingSendComment)
                is CommentUiState.Success -> {
                    dispatch(ShowEvent(true, "Комментарий успешно добавлен"))
                    scope.launch {
                        service.startComments(startId)
                            .onSuccess {
                                dispatch(Loaded(it))
                            }
                            .onFailure {
                                dispatch(Error(it))
                            }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is LoadingComments -> copy(isLoadingComments = true, error = null)
                is LoadingSendComment -> copy(isLoadingSendComment = true)
                is OnClickCloseReply -> copy(mode = CommentMode.Base)
                is OnClickReply -> {
                    val comment = comments.rows.find { it.id == msg.commentId }
                    if (comment != null) {
                        val commentData = com.markettwits.sportsouce.start.presentation.start.component.CommentData(
                            userName = "${comment.user.surname} ${comment.user.name}",
                            userPhoto = comment.user.photo ?: "",
                            commentText = comment.comment,
                            createdAt = comment.createdAt,
                            replies = comment.replies.map { reply ->
                                com.markettwits.sportsouce.start.presentation.start.component.ReplyData(
                                    userName = "${reply.user.surname} ${reply.user.name}",
                                    userPhoto = reply.user.photo ?: "",
                                    commentText = reply.comment,
                                    createdAt = reply.createdAt
                                )
                            }
                        )
                        copy(
                            mode = Reply(
                                replier = msg.replier,
                                messageId = msg.commentId,
                                parentComment = commentData
                            )
                        )
                    } else {
                        this
                    }
                }

                is OnClickSendComment -> copy(isLoadingSendComment = true)
                is ShowEvent -> copy(
                    event = triggered(
                        EventContent(
                            success = msg.success,
                            message = msg.message
                        )
                    ),
                    isLoadingSendComment = false
                )

                is Loaded -> copy(comments = msg.state, isLoadingComments = false, error = null)
                is Error -> copy(error = msg.error.mapToSauceError(), isLoadingComments = false)
                is OnConsumedEvent -> copy(event = consumed())
                is UpdateStartId -> copy(startId = msg.startId)
            }
    }
}
