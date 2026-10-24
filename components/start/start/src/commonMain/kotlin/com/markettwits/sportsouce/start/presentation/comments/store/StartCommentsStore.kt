package com.markettwits.sportsouce.start.presentation.comments.store

import com.arkivanov.mvikotlin.core.store.Reducer
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
import com.markettwits.sportsouce.start.presentation.start.component.CommentMode
import com.markettwits.sportsouce.start.presentation.start.component.CommentUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.currentCoroutineContext

enum class StartCommentsFilter {
    All, Unanswered, Reviews, Answered;

    fun matches(comment: StartItem.Comments.Row): Boolean = when (this) {
        All -> true
        Unanswered -> comment.isQuestion && !comment.isAnswered
        Reviews -> comment.isReview
        Answered -> comment.isQuestion && comment.isAnswered
    }
}

interface StartCommentsStore : Store<StartCommentsStore.Intent, StartCommentsStore.State, StartCommentsStore.Label> {
    sealed interface Intent {
        data class ApplyStartId(val startId: Int) : Intent
        data class OnClickReply(val replier: String, val commentId: Int) : Intent
        data object OnClickCloseReply : Intent
        data object OnConsumedEvent : Intent
        data class OnCommentChanged(val value: String) : Intent
        data class OnQuestionChanged(val value: Boolean) : Intent
        data class OnClickSendComment(val value: String) : Intent
        data object OnClickGoBack : Intent
        data class OnClickShowReplies(val commentId: Int) : Intent
        data object OnRefresh : Intent
        data class OnFilterChanged(val filter: StartCommentsFilter) : Intent
    }

    data class State(
        val startId: Int? = null,
        val comments: StartItem.Comments = StartItem.Comments(0, emptyList()),
        val mode: CommentMode = CommentMode.Base,
        val comment: String = "",
        val isQuestion: Boolean = false,
        val sentCommentVersion: Int = 0,
        val isLoadingComments: Boolean = false,
        val isLoadingSendComment: Boolean = false,
        val error: SauceError? = null,
        val event: StateEventWithContent<EventContent> = consumed(),
        val filter: StartCommentsFilter = StartCommentsFilter.All,
    ) {
        internal val visibleComments get() = comments.rows.filter(filter::matches)
        internal val unansweredCount get() = comments.rows.count(StartCommentsFilter.Unanswered::matches)
    }

    sealed interface Label {
        data object OnGoBack : Label
        data class OnNavigateToReplies(val commentId: Int) : Label
    }
}

class StartCommentsStoreFactory(
    private val storeFactory: StoreFactory,
    private val service: StartRepository,
) {
    fun create(mode: CommentMode = CommentMode.Base): StartCommentsStore =
        object : StartCommentsStore, Store<StartCommentsStore.Intent, StartCommentsStore.State, StartCommentsStore.Label> by storeFactory.create(
            name = "CommentsStore",
            initialState = StartCommentsStore.State(mode = mode),
            executorFactory = { ExecutorImpl() },
            reducer = ReducerImpl,
        ) {}

    private sealed interface Msg {
        data class StartId(val id: Int) : Msg
        data class Draft(val value: String) : Msg
        data class Question(val value: Boolean) : Msg
        data class Filter(val value: StartCommentsFilter) : Msg
        data class Event(val success: Boolean, val message: String) : Msg
        data object Consumed : Msg
        data object Loading : Msg
        data object Sending : Msg
        data class Loaded(val comments: StartItem.Comments) : Msg
        data class Error(val error: Throwable) : Msg
    }

    private inner class ExecutorImpl : CoroutineExecutor<StartCommentsStore.Intent, Unit, StartCommentsStore.State, Msg, StartCommentsStore.Label>() {
        private var loadJob: Job? = null

        override fun executeIntent(intent: StartCommentsStore.Intent) {
            when (intent) {
                is StartCommentsStore.Intent.ApplyStartId -> {
                    dispatch(Msg.StartId(intent.startId))
                    load(intent.startId)
                }
                is StartCommentsStore.Intent.OnCommentChanged -> if (!state().isLoadingSendComment) dispatch(Msg.Draft(intent.value))
                is StartCommentsStore.Intent.OnQuestionChanged -> if (!state().isLoadingSendComment) dispatch(Msg.Question(intent.value))
                is StartCommentsStore.Intent.OnFilterChanged -> dispatch(Msg.Filter(intent.filter))
                is StartCommentsStore.Intent.OnClickCloseReply -> if (!state().isLoadingSendComment) publish(StartCommentsStore.Label.OnGoBack)
                is StartCommentsStore.Intent.OnClickReply -> if (!state().isLoadingSendComment) {
                    state().comments.rows.find { it.id == intent.commentId }?.let {
                        publish(StartCommentsStore.Label.OnNavigateToReplies(it.id))
                    }
                }
                is StartCommentsStore.Intent.OnClickSendComment -> state().startId?.let {
                    if (!state().isLoadingSendComment && intent.value.isNotBlank()) send(it, intent.value.trim())
                }
                is StartCommentsStore.Intent.OnConsumedEvent -> dispatch(Msg.Consumed)
                is StartCommentsStore.Intent.OnClickGoBack -> publish(StartCommentsStore.Label.OnGoBack)
                is StartCommentsStore.Intent.OnClickShowReplies -> publish(StartCommentsStore.Label.OnNavigateToReplies(intent.commentId))
                is StartCommentsStore.Intent.OnRefresh -> state().startId?.let(::load)
            }
        }

        private fun load(startId: Int) {
            loadJob?.cancel()
            dispatch(Msg.Loading)
            loadJob = scope.launch {
                val result = service.startComments(startId)
                currentCoroutineContext().ensureActive()
                result.onSuccess { dispatch(Msg.Loaded(it)) }
                    .onFailure { if (it !is CancellationException) dispatch(Msg.Error(it)) }
            }
        }

        private fun send(startId: Int, comment: String) {
            val mode = state().mode
            val question = state().isQuestion && mode is CommentMode.Base
            dispatch(Msg.Sending)
            scope.launch {
                val result = try {
                    service.writeComment(
                        startId = startId,
                        comment = comment,
                        id = (mode as? CommentMode.Reply)?.messageId ?: startId,
                        subComment = mode is CommentMode.Reply,
                        isQuestion = question,
                    )
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    CommentUiState.Error("Не удалось отправить комментарий. Попробуйте ещё раз.")
                }
                currentCoroutineContext().ensureActive()
                when (result) {
                    is CommentUiState.Success -> {
                        dispatch(Msg.Event(true, if (mode is CommentMode.Reply) "Ответ отправлен" else "Комментарий отправлен"))
                        // The server decides whether a reply is an administrator's answer.
                        load(startId)
                    }
                    is CommentUiState.Error -> dispatch(Msg.Event(false, result.message))
                    is CommentUiState.Loading -> dispatch(Msg.Event(false, "Не удалось отправить комментарий. Попробуйте ещё раз."))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<StartCommentsStore.State, Msg> {
        override fun StartCommentsStore.State.reduce(msg: Msg): StartCommentsStore.State = when (msg) {
            is Msg.StartId -> copy(startId = msg.id)
            is Msg.Draft -> copy(comment = msg.value)
            is Msg.Question -> copy(isQuestion = msg.value)
            is Msg.Filter -> copy(filter = msg.value)
            is Msg.Loading -> copy(isLoadingComments = true, error = null)
            is Msg.Sending -> copy(isLoadingSendComment = true)
            is Msg.Loaded -> copy(comments = msg.comments, isLoadingComments = false, error = null)
            is Msg.Error -> copy(error = msg.error.mapToSauceError(), isLoadingComments = false)
            is Msg.Consumed -> copy(event = consumed())
            is Msg.Event -> copy(
                event = triggered(EventContent(success = msg.success, message = msg.message)),
                isLoadingSendComment = false,
                comment = if (msg.success) "" else comment,
                isQuestion = if (msg.success) false else isQuestion,
                filter = if (msg.success && mode is CommentMode.Base) StartCommentsFilter.All else filter,
                sentCommentVersion = sentCommentVersion + if (msg.success) 1 else 0,
            )
        }
    }
}
