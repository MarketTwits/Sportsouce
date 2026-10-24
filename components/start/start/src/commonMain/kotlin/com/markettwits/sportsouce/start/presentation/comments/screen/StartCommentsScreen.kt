package com.markettwits.sportsouce.start.presentation.comments.screen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markettwits.core.errors.api.composable.SauceErrorScreen
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.components.navigation.SauceLazyVerticalGrid
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.components.selection.SauceCheckboxRow
import com.markettwits.core_ui.items.components.snackbar.SauceSnackbarHost
import com.markettwits.core_ui.items.components.snackbar.SauceSnackbarTone
import com.markettwits.core_ui.items.components.topbar.SauceScaffold
import com.markettwits.core_ui.items.components.topbar.TopBarBase
import com.markettwits.core_ui.items.event.EventEffect
import com.markettwits.core_ui.items.extensions.showLongMessageWithDismiss
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.presentation.comments.component.StartCommentsComponent
import com.markettwits.sportsouce.start.presentation.comments.components.StartCommentCard
import com.markettwits.sportsouce.start.presentation.comments.components.StartCommentTextField
import com.markettwits.sportsouce.start.presentation.comments.store.StartCommentsFilter
import com.markettwits.sportsouce.start.presentation.comments.store.StartCommentsStore
import com.markettwits.sportsouce.start.presentation.start.component.CommentMode
import com.markettwits.sportsouce.start.presentation.start.component.toCommentData

@Composable
fun StartCommentsScreen(component: StartCommentsComponent) {
    val state by component.state.collectAsState()
    StartCommentsContent(state, component::obtainEvent)
}

@Composable
internal fun StartCommentsContent(state: StartCommentsStore.State, onIntent: (StartCommentsStore.Intent) -> Unit) {
    val snackbar = remember { SnackbarHostState() }
    var tone by remember { mutableStateOf(SauceSnackbarTone.Neutral) }
    val keyboard = LocalSoftwareKeyboardController.current
    LaunchedEffect(state.sentCommentVersion) {
        if (state.sentCommentVersion > 0) keyboard?.hide()
    }
    SauceScaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopBarBase(title = if (state.mode is CommentMode.Reply) "Ответы" else "Комментарии",
                goBack = { onIntent(StartCommentsStore.Intent.OnClickGoBack) })
        },
        snackbarHost = { SauceSnackbarHost(snackbar, tone = tone) },
        bottomBar = {
            Surface(color = SauceTheme.colors.surface) {
                Column(Modifier.navigationBarsPadding()) {
                    if (state.mode is CommentMode.Base) {
                        SauceCheckboxRow(
                            checked = state.isQuestion,
                            enabled = !state.isLoadingSendComment,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            onCheckedChange = { onIntent(StartCommentsStore.Intent.OnQuestionChanged(it)) },
                            label = { Text("Нужен ответ администратора", style = MaterialTheme.typography.bodyMedium) },
                        )
                    }
                    StartCommentTextField(
                        comment = state.comment,
                        onCommentChanged = { onIntent(StartCommentsStore.Intent.OnCommentChanged(it)) },
                        mode = state.mode,
                        isLoading = state.isLoadingSendComment,
                        onClickCloseReply = { onIntent(StartCommentsStore.Intent.OnClickCloseReply) },
                        sendComment = { onIntent(StartCommentsStore.Intent.OnClickSendComment(it)) },
                    )
                }
            }
        },
    ) { padding ->
        val modifier = Modifier.fillMaxSize().padding(padding)
        if (state.error != null && state.comments.rows.isEmpty() && state.mode is CommentMode.Base) {
            state.error.SauceErrorScreen(modifier, onClickRetry = { onIntent(StartCommentsStore.Intent.OnRefresh) })
        } else {
            PullToRefreshScreen(
                modifier = modifier,
                isRefreshing = state.isLoadingComments && state.comments.rows.isNotEmpty(),
                onRefresh = { onIntent(StartCommentsStore.Intent.OnRefresh) },
            ) {
                BoxWithConstraints(Modifier.fillMaxSize()) {
                    val viewportWidth = maxWidth
                    SauceLazyVerticalGrid(
                        columns = GridCells.Adaptive(320.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        if (state.mode is CommentMode.Base) {
                            item(key = "filters", span = { GridItemSpan(maxLineSpan) }) {
                                StartCommentsFilters(
                                    selected = state.filter,
                                    unansweredCount = state.unansweredCount,
                                    // Full-span grid items still exclude the grid's content padding.
                                    modifier = Modifier.requiredWidth(viewportWidth),
                                ) {
                                    onIntent(StartCommentsStore.Intent.OnFilterChanged(it))
                                }
                            }
                        }
                        if (state.error != null) {
                            item(key = "refresh_error", span = { GridItemSpan(maxLineSpan) }) {
                                Column {
                                    Text("Не удалось обновить комментарии", color = SauceTheme.colors.danger.content,
                                        style = MaterialTheme.typography.bodyMedium)
                                    SauceButton("Повторить", { onIntent(StartCommentsStore.Intent.OnRefresh) }, variant = SauceButtonVariant.Text)
                                }
                            }
                        }
                        if (state.isLoadingComments && state.comments.rows.isEmpty() && state.mode is CommentMode.Base) {
                            items(3) { CommentSkeleton() }
                        } else when (val mode = state.mode) {
                            is CommentMode.Base -> {
                                if (state.visibleComments.isEmpty()) {
                                    item(key = "empty", span = { GridItemSpan(maxLineSpan) }) {
                                        EmptyCommentsView(state.filter)
                                    }
                                }
                                items(state.visibleComments, key = { "comment_${it.id}" }) { comment ->
                                    StartCommentCard(
                                        userImageUrl = comment.user.photo.orEmpty(),
                                        userName = "${comment.user.surname} ${comment.user.name}".trim(),
                                        commentCreateDate = comment.createdAt,
                                        message = comment.comment,
                                        isQuestion = comment.isQuestion,
                                        isAnswered = comment.isAnswered,
                                        isReview = comment.isReview,
                                        averageScore = comment.averageScore,
                                        replies = comment.replies,
                                        actionsEnabled = !state.isLoadingSendComment,
                                        onClickReply = { onIntent(StartCommentsStore.Intent.OnClickReply(comment.user.name, comment.id)) },
                                        onClickShowReplies = { onIntent(StartCommentsStore.Intent.OnClickShowReplies(comment.id)) },
                                    )
                                }
                            }
                            is CommentMode.Reply -> {
                                val parent = state.comments.rows.find { it.id == mode.messageId }?.toCommentData() ?: mode.parentComment
                                item(key = "parent", span = { GridItemSpan(maxLineSpan) }) {
                                    StartCommentCard(
                                        userImageUrl = parent.userPhoto, userName = parent.userName,
                                        commentCreateDate = parent.createdAt, message = parent.commentText,
                                        isQuestion = parent.isQuestion, isAnswered = parent.isAnswered,
                                        isReview = parent.isReview, averageScore = parent.averageScore,
                                        isReply = true, onClickReply = {},
                                    )
                                }
                                items(parent.replies.size, key = { "reply_${parent.replies[it].id}_$it" }) { index ->
                                    val reply = parent.replies[index]
                                    StartCommentCard(
                                        modifier = Modifier.padding(start = 12.dp),
                                        userImageUrl = reply.userPhoto, userName = reply.userName,
                                        commentCreateDate = reply.createdAt, message = reply.commentText,
                                        isReply = true, isAdminAnswer = reply.isAdminAnswer, onClickReply = {},
                                    )
                                }
                                if (parent.replies.isEmpty()) item(key = "no_replies", span = { GridItemSpan(maxLineSpan) }) {
                                    Text("Ответов пока нет", Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium,
                                        color = SauceTheme.colors.textMuted)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    EventEffect(event = state.event, onConsumed = { onIntent(StartCommentsStore.Intent.OnConsumedEvent) }) {
        tone = if (it.success) SauceSnackbarTone.Success else SauceSnackbarTone.Error
        snackbar.showLongMessageWithDismiss(it.message)
    }
}

@Composable
internal fun StartCommentsFilters(
    selected: StartCommentsFilter,
    unansweredCount: Int,
    modifier: Modifier = Modifier,
    onSelected: (StartCommentsFilter) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StartCommentsFilter.entries.forEach { filter ->
            FilterChipBase(
                selected = selected == filter,
                selectedColors = when (filter) {
                    StartCommentsFilter.Unanswered -> SauceTheme.colors.warning
                    StartCommentsFilter.Answered -> SauceTheme.colors.success
                    else -> null
                },
                label = when (filter) {
                    StartCommentsFilter.All -> "Все"
                    StartCommentsFilter.Unanswered -> "Без ответа · $unansweredCount"
                    StartCommentsFilter.Reviews -> "Отзывы"
                    StartCommentsFilter.Answered -> "Отвеченные"
                },
                onClick = { onSelected(filter) },
            )
        }
    }
}

@Composable
private fun CommentSkeleton() {
    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            SauceSkeleton(Modifier.size(36.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                SauceSkeleton(Modifier.width(120.dp).height(14.dp))
                SauceSkeleton(Modifier.width(90.dp).height(10.dp))
            }
        }
        SauceSkeleton(Modifier.fillMaxWidth().height(14.dp))
        SauceSkeleton(Modifier.fillMaxWidth(.7f).height(14.dp))
    }
}

@Composable
private fun EmptyCommentsView(filter: StartCommentsFilter) {
    Column(Modifier.fillMaxWidth().padding(vertical = 32.dp), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Icon(Icons.Default.ChatBubbleOutline, null, Modifier.size(36.dp), tint = SauceTheme.colors.textMuted)
        Text(when (filter) {
            StartCommentsFilter.All -> "Комментариев пока нет"
            StartCommentsFilter.Unanswered -> "Нет вопросов без ответа"
            StartCommentsFilter.Reviews -> "Отзывов пока нет"
            StartCommentsFilter.Answered -> "Отвеченных вопросов пока нет"
        }, style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary, textAlign = TextAlign.Center)
        if (filter == StartCommentsFilter.All) Text("Оставьте комментарий или задайте вопрос о старте",
            style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textMuted, textAlign = TextAlign.Center)
    }
}
