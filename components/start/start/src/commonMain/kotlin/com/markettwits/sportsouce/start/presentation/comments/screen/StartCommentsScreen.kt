package com.markettwits.sportsouce.start.presentation.comments.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core.errors.api.composable.SauceErrorScreen
import com.markettwits.core_ui.items.components.topbar.TopBarBase
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.screens.LoadingFullScreen
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.presentation.comments.component.StartCommentsComponent
import com.markettwits.sportsouce.start.presentation.comments.components.StartCommentCard
import com.markettwits.sportsouce.start.presentation.comments.components.StartCommentTextField
import com.markettwits.sportsouce.start.presentation.comments.store.StartCommentsStore
import com.markettwits.sportsouce.start.presentation.start.component.CommentMode

@Composable
fun StartCommentsScreen(
    component: StartCommentsComponent,
) {
    val state by component.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    var snackBarColor by remember {
        mutableStateOf(SportSouceColor.SportSouceLightRed)
    }

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.ime),
        topBar = {
            TopBarBase(
                title = when (state.mode) {
                    is CommentMode.Reply -> "Ответы"
                    is CommentMode.Base -> "Комментарии"
                },
                goBack = {
                    component.obtainEvent(StartCommentsStore.Intent.OnClickGoBack)
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
            ) {
                Snackbar(
                    contentColor = Color.White,
                    containerColor = snackBarColor,
                    snackbarData = it
                )
            }
        },
        bottomBar = {
            StartCommentTextField(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars),
                mode = state.mode,
                onClickCloseReply = {
                    component.obtainEvent(StartCommentsStore.Intent.OnClickCloseReply)
                },
                isLoading = state.isLoadingSendComment,
            ) { comment ->
                component.obtainEvent(StartCommentsStore.Intent.OnClickSendComment(comment))
            }
        }
    ) { paddingValues ->
        when {
            state.error != null && state.comments.rows.isEmpty() -> {
                state.error!!.SauceErrorScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = paddingValues.calculateTopPadding(),
                            bottom = paddingValues.calculateBottomPadding(),
                        ),
                    onClickRetry = {
                        component.obtainEvent(StartCommentsStore.Intent.OnRefresh)
                    }
                )
            }

            state.isLoadingComments && state.comments.rows.isEmpty() -> {
                LoadingFullScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = paddingValues.calculateTopPadding(),
                            bottom = paddingValues.calculateBottomPadding(),
                        )
                )
            }

            else -> {
                AdaptivePane {
                    when (state.mode) {
                        is CommentMode.Base -> {
                            if (state.comments.rows.isEmpty()) {
                                EmptyCommentsView(
                                    modifier = Modifier
                                        .padding(
                                            top = paddingValues.calculateTopPadding(),
                                            bottom = paddingValues.calculateBottomPadding(),
                                        )
                                        .fillMaxSize()
                                )
                            } else {
                                PullToRefreshScreen(
                                    modifier = Modifier.padding(
                                        top = paddingValues.calculateTopPadding(),
                                        bottom = paddingValues.calculateBottomPadding(),
                                    ),
                                    isRefreshing = state.isLoadingComments,
                                    onRefresh = {
                                        component.obtainEvent(StartCommentsStore.Intent.OnRefresh)
                                    }
                                ) {
                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                                    ) {
                                        items(state.comments.rows) { comment ->
                                            StartCommentCard(
                                                modifier = Modifier.padding(vertical = 4.dp),
                                                userImageUrl = comment.user.photo ?: "",
                                                userName = "${comment.user.surname} ${comment.user.name}",
                                                commentCreateDate = comment.createdAt,
                                                message = comment.comment,
                                                onClickReply = {
                                                    component.obtainEvent(
                                                        StartCommentsStore.Intent.OnClickReply(
                                                            replier = comment.user.name,
                                                            commentId = comment.id
                                                        )
                                                    )
                                                },
                                                replies = comment.replies,
                                                showReplies = true,
                                                onClickShowReplies = {
                                                    component.obtainEvent(
                                                        StartCommentsStore.Intent.OnClickShowReplies(comment.id)
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        is CommentMode.Reply -> {
                            val replyMode = state.mode as CommentMode.Reply
                            val parentComment = state.comments.rows.find { it.id == replyMode.messageId }
                            PullToRefreshScreen(
                                modifier = Modifier.padding(
                                    top = paddingValues.calculateTopPadding(),
                                    bottom = paddingValues.calculateBottomPadding(),
                                ),
                                isRefreshing = state.isLoadingComments,
                                onRefresh = {
                                    component.obtainEvent(StartCommentsStore.Intent.OnRefresh)
                                }
                            ) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                                ) {
                                    item {
                                        StartCommentCard(
                                            modifier = Modifier.padding(vertical = 4.dp),
                                            userImageUrl = parentComment?.user?.photo
                                                ?: replyMode.parentComment.userPhoto,
                                            userName = if (parentComment != null) "${parentComment.user.surname} ${parentComment.user.name}" else replyMode.parentComment.userName,
                                            commentCreateDate = parentComment?.createdAt
                                                ?: replyMode.parentComment.createdAt,
                                            message = parentComment?.comment ?: replyMode.parentComment.commentText,
                                            onClickReply = {},
                                            replies = emptyList(),
                                            showReplies = false,
                                            isReply = false
                                        )
                                    }

                                    items(parentComment?.replies ?: replyMode.parentComment.replies.map { reply ->
                                        StartItem.Comments.Reply(
                                            id = 0,
                                            comment = reply.commentText,
                                            createdAt = reply.createdAt,
                                            user = StartItem.Comments.User(
                                                id = 0,
                                                name = reply.userName.split(" ").getOrNull(1) ?: "",
                                                surname = reply.userName.split(" ").getOrNull(0) ?: "",
                                                photo = reply.userPhoto
                                            )
                                        )
                                    }) { reply ->
                                        StartCommentCard(
                                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                                            userImageUrl = reply.user.photo ?: "",
                                            userName = "${reply.user.surname} ${reply.user.name}",
                                            commentCreateDate = reply.createdAt,
                                            message = reply.comment,
                                            onClickReply = {},
                                            replies = emptyList(),
                                            showReplies = false,
                                            isReply = true
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    EventEffect(
        event = state.event,
        onConsumed = {
            component.obtainEvent(StartCommentsStore.Intent.OnConsumedEvent)
        },
    ) {
        snackBarColor =
            if (it.success) SportSouceColor.SportSouceLighBlue else SportSouceColor.SportSouceLightRed
        snackBarHostState.showLongMessageWithDismiss(message = it.message)
    }
}

@Composable
private fun StartCommentCard(
    modifier: Modifier = Modifier,
    userImageUrl: String,
    userName: String,
    commentCreateDate: String,
    message: String,
    replies: List<StartItem.Comments.Reply>,
    onClickReply: () -> Unit,
    showReplies: Boolean = false,
    isReply: Boolean = false,
    onClickShowReplies: (() -> Unit)? = null,
) {
    StartCommentCard(
        modifier = modifier,
        userImageUrl = userImageUrl,
        userName = userName,
        commentCreateDate = commentCreateDate,
        message = message,
        onClickReply = onClickReply,
        replies = replies,
        isReply = isReply,
        showRepliesButton = showReplies,
        onClickShowReplies = onClickShowReplies
    )
}

@Composable
private fun EmptyCommentsView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ChatBubbleOutline,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Комментариев пока нет",
            fontSize = 18.sp,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Будьте первым, кто оставит комментарий\nили задаст вопрос!",
            fontSize = 14.sp,
            fontFamily = FontNunito.regular(),
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center
        )
    }
}
