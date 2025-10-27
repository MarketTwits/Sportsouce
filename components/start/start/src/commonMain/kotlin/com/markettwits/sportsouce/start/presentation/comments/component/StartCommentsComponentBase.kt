package com.markettwits.sportsouce.start.presentation.comments.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.markettwits.core.decompose.componentScope
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarComponentHandler
import com.markettwits.sportsouce.bottom_bar.component.listener.BottomBarVisibilityStrategy
import com.markettwits.sportsouce.start.presentation.comments.store.StartCommentsStore
import com.markettwits.sportsouce.start.presentation.comments.store.StartCommentsStoreFactory
import com.markettwits.sportsouce.start.presentation.start.component.CommentMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class StartCommentsComponentBase(
    context: ComponentContext,
    private val storeFactory: StartCommentsStoreFactory,
    private val startId: Int? = null,
    mode: CommentMode = CommentMode.Base,
    private val onBack: () -> Unit = {},
    private val onNavigateToReplies: (Int, String, com.markettwits.sportsouce.start.presentation.start.component.CommentData) -> Unit = { _, _, _ -> },
) : ComponentContext by context, StartCommentsComponent, BottomBarComponentHandler() {

    private val store = instanceKeeper.getStore {
        storeFactory.create(mode)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<StartCommentsStore.State> = store.stateFlow

    override fun obtainEvent(intent: StartCommentsStore.Intent) {
        store.accept(intent)
    }

    init {
        subscribeOnBottomBar(BottomBarVisibilityStrategy.AlwaysInvisible)
        store.labels.onEach { label ->
            when (label) {
                StartCommentsStore.Label.OnGoBack -> onBack()
                is StartCommentsStore.Label.OnNavigateToReplies -> {
                    val currentState = store.state
                    val comment = currentState.comments.rows.find { it.id == label.commentId }
                    comment?.let {
                        val commentData = com.markettwits.sportsouce.start.presentation.start.component.CommentData(
                            userName = "${it.user.surname} ${it.user.name}",
                            userPhoto = it.user.photo ?: "",
                            commentText = it.comment,
                            createdAt = it.createdAt,
                            replies = it.replies.map { reply ->
                                com.markettwits.sportsouce.start.presentation.start.component.ReplyData(
                                    userName = "${reply.user.surname} ${reply.user.name}",
                                    userPhoto = reply.user.photo ?: "",
                                    commentText = reply.comment,
                                    createdAt = reply.createdAt
                                )
                            }
                        )
                        onNavigateToReplies(label.commentId, it.user.name, commentData)
                    }
                }
            }
        }.launchIn(componentScope)

        startId?.let {
            store.accept(StartCommentsStore.Intent.ApplyStartId(it))
        }
    }
}