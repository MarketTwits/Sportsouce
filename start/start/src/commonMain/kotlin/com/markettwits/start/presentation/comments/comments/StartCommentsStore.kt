package com.markettwits.start.presentation.comments.comments

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.core_ui.items.event.EventContent
import com.markettwits.core_ui.items.event.StateEventWithContent
import com.markettwits.core_ui.items.event.consumed
import com.markettwits.core_ui.items.event.triggered
import com.markettwits.start.domain.StartItem
import com.markettwits.start.domain.StartRepository
import com.markettwits.start.presentation.comments.comments.StartCommentsStore.Intent
import com.markettwits.start.presentation.comments.comments.StartCommentsStore.Label
import com.markettwits.start.presentation.comments.comments.StartCommentsStore.State
import com.markettwits.start.presentation.start.component.CommentMode
import com.markettwits.start.presentation.start.component.CommentUiState
import kotlinx.coroutines.launch

interface StartCommentsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class OnClickReply(val replier: String, val commentId: Int) : Intent
        data object OnClickCloseReply : Intent
        data object OnConsumedEvent : Intent
        data class OnClickSendComment(val value: String) : Intent
    }

    data class State(
        val comments: StartItem.Comments = StartItem.Comments(
            0,
            emptyList()
        ),
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

    fun create(startId: Int): StartCommentsStore =
        object : StartCommentsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "CommentsStore",
            initialState = State(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = { ExecutorImpl(startId) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data class OnClickReply(val replier: String, val commentId: Int) : Msg
        data object OnClickCloseReply : Msg
        data class OnClickSendComment(val value: String) : Msg
        data class ShowEvent(val success: Boolean, val message: String) : Msg
        data object OnConsumedEvent : Msg
        data object Loading : Msg
        data class Loaded(val state: StartItem.Comments) : Msg
    }

    private inner class ExecutorImpl(private val startId: Int) :
        CoroutineExecutor<Intent, Unit, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.OnClickCloseReply -> dispatch(Msg.OnClickCloseReply)
                is Intent.OnClickReply -> dispatch(
                    Msg.OnClickReply(
                        intent.replier,
                        intent.commentId
                    )
                )

                is Intent.OnClickSendComment -> sendComment(getState().mode, intent.value, startId)
                is Intent.OnConsumedEvent -> dispatch(Msg.OnConsumedEvent)
            }
        }

        override fun executeAction(action: Unit, getState: () -> State) {
            launch(startId)
        }

        private fun launch(startId: Int) {
            scope.launch {
                service.startComments(startId).onSuccess {
                    dispatch(Msg.Loaded(it))
                }
            }
        }

        private fun sendComment(mode: CommentMode, comment: String, startId: Int) {
            dispatch(Msg.Loading)
            scope.launch {
                val value = if (mode is CommentMode.Reply) {
                    service.writeComment(
                        comment = comment,
                        id = mode.messageId,
                        subComment = true
                    )

                } else {
                    service.writeComment(
                        comment = comment,
                        id = startId,
                        subComment = false
                    )
                }
                handleCommentUiState(value, startId)
            }
        }

        private fun handleCommentUiState(value: CommentUiState, startId: Int) {
            when (value) {
                is CommentUiState.Error -> dispatch(Msg.ShowEvent(false, value.message))
                is CommentUiState.Loading -> dispatch(Msg.Loading)
                is CommentUiState.Success -> {
                    dispatch(Msg.ShowEvent(true, "Комментарий успешно добавлен"))
                    launch(startId)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(message: Msg): State =
            when (message) {
                is Msg.Loading -> copy(isLoading = true)
                is Msg.OnClickCloseReply -> copy(mode = CommentMode.Base)
                is Msg.OnClickReply -> copy(
                    mode = CommentMode.Reply(
                        replier = message.replier,
                        messageId = message.commentId
                    )
                )

                is Msg.OnClickSendComment -> copy(isLoading = true)
                is Msg.ShowEvent -> copy(
                    event = triggered(
                        EventContent(
                            success = message.success,
                            message.message
                        )
                    ),
                    isLoading = false
                )

                is Msg.Loaded -> copy(comments = message.state, isLoading = false, isError = false)
                is Msg.OnConsumedEvent -> copy(event = consumed())
            }
    }
}
