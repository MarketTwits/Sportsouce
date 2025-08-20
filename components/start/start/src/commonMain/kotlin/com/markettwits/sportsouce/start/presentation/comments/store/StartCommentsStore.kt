package com.markettwits.sportsouce.start.presentation.comments.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
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
    }

    data class State(
        val startId: Int? = null,
        val comments: StartItem.Comments = StartItem.Comments(0, emptyList()),
        val mode: CommentMode = CommentMode.Base,
        val comment: String = "",
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val event: StateEventWithContent<EventContent> = consumed(),
    )

    sealed interface Label
}

class StartCommentsStoreFactory(
    private val storeFactory: StoreFactory,
    private val service: StartRepository
) {

    fun create(): StartCommentsStore =
        object : StartCommentsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "CommentsStore",
            initialState = State(),
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
        data object Loading : Msg
        data class Loaded(val state: StartItem.Comments) : Msg
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

            }
        }

        private fun launch(startId: Int) {
            scope.launch {
                service.startComments(startId).onSuccess {
                    dispatch(Loaded(it))
                }
            }
        }

        private fun sendComment(mode: CommentMode, comment: String, startId: Int) {
            dispatch(Loading)
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
                is CommentUiState.Error -> dispatch(ShowEvent(false, value.message))
                is CommentUiState.Loading -> dispatch(Loading)
                is CommentUiState.Success -> {
                    dispatch(ShowEvent(true, "Комментарий успешно добавлен"))
                    launch(startId)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Loading -> copy(isLoading = true)
                is OnClickCloseReply -> copy(mode = CommentMode.Base)
                is OnClickReply -> copy(
                    mode = Reply(
                        replier = msg.replier,
                        messageId = msg.commentId
                    )
                )

                is OnClickSendComment -> copy(isLoading = true)
                is ShowEvent -> copy(
                    event = triggered(
                        EventContent(
                            success = msg.success,
                            message = msg.message
                        )
                    ),
                    isLoading = false
                )

                is Loaded -> copy(comments = msg.state, isLoading = false, isError = false)
                is OnConsumedEvent -> copy(event = consumed())
                is UpdateStartId -> copy(startId = msg.startId)
            }
    }
}
