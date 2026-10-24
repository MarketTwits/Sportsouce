package com.markettwits.sportsouce.start.presentation.comments.components

import androidx.compose.animation.AnimatedVisibility
import kotlin.math.roundToInt
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.Icon
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.components.status.SauceStatusBadge
import com.markettwits.core_ui.items.components.status.SauceStatusTone
import com.markettwits.core_ui.items.theme.SauceTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.text.ClickableText
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.start.domain.StartItem


@Composable
fun StartCommentsCompactPanel(
    modifier: Modifier = Modifier,
    comments: StartItem.Comments,
    maxComments: Int = 5,
    onClickViewAll: () -> Unit,
    onClickReply: (String, Int) -> Unit,
    onClickTextField: () -> Unit,
) {
    Column(modifier = modifier) {
        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            itemVerticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Комментарии", style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
            if (comments.rows.isNotEmpty()) {
                SauceButton("Все · ${comments.rows.size}", onClickViewAll,
                    variant = SauceButtonVariant.Text, fillWidth = false)
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (comments.rows.isNotEmpty()) {
                val displayComments = comments.rows.take(maxComments)
                displayComments.forEach {
                    StartCommentCard(
                        modifier = Modifier,
                        userImageUrl = it.user.photo ?: "",
                        userName = "${it.user.surname} ${it.user.name}",
                        commentCreateDate = it.createdAt,
                        message = it.comment,
                        isQuestion = it.isQuestion,
                        isAnswered = it.isAnswered,
                        isReview = it.isReview,
                        averageScore = it.averageScore,
                        onClickReply = {
                            onClickReply(it.user.name, it.id)
                        },
                        replies = it.replies,
                        showRepliesButton = false,
                        onClickShowReplies = null
                    )
                }
            } else {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 16.dp),
                    text = "Комментариев пока нет",
                    fontSize = 15.sp,
                    fontFamily = FontNunito.medium(),
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        StartCommentTextField(
            modifier = Modifier.padding(top = 8.dp),
            isLoading = false,
            mode = com.markettwits.sportsouce.start.presentation.start.component.CommentMode.Base,
            readOnly = true,
            onClickTextField = onClickTextField,
            onClickCloseReply = {},
            sendComment = {}
        )
    }
}

@Composable
internal fun StartCommentCard(
    modifier: Modifier = Modifier,
    userImageUrl: String,
    userName: String,
    commentCreateDate: String,
    message: String,
    replies: List<StartItem.Comments.Reply> = emptyList(),
    onClickReply: () -> Unit,
    isReply: Boolean = false,
    showRepliesButton: Boolean = true,
    onClickShowReplies: (() -> Unit)? = null,
    isQuestion: Boolean = false,
    isAnswered: Boolean = false,
    isReview: Boolean = false,
    averageScore: Double? = null,
    isAdminAnswer: Boolean = false,
    actionsEnabled: Boolean = true,
) {
    var showReply by rememberSaveable { mutableStateOf(false) }
    var showAvatar by rememberSaveable { mutableStateOf(false) }
    val colors = SauceTheme.colors
    if (showAvatar && userImageUrl.isNotBlank()) FullImageScreen(image = userImageUrl) { showAvatar = false }

    OnBackgroundCard(modifier) {
        Column(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                if (userImageUrl.isNotBlank()) {
                    SubcomposeAsyncImage(
                        model = userImageUrl,
                        contentDescription = "Фото: $userName",
                        modifier = Modifier.size(36.dp).clip(CircleShape).clickable { showAvatar = true },
                        contentScale = ContentScale.Crop,
                        loading = { SauceSkeleton(Modifier.fillMaxSize(), CircleShape) },
                        error = { EmptyAvatar() },
                    )
                } else EmptyAvatar()
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(userName, style = MaterialTheme.typography.titleSmall, color = colors.textPrimary)
                    Text(commentCreateDate, style = MaterialTheme.typography.bodySmall, color = colors.textMuted)
                }
            }
            if (isQuestion || isReview || isAdminAnswer) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (isQuestion) {
                        SauceStatusBadge("Вопрос", tone = if (isAnswered) SauceStatusTone.Success else SauceStatusTone.Warning, showIndicator = false)
                        SauceStatusBadge(if (isAnswered) "Есть ответ" else "Ждёт ответа",
                            tone = if (isAnswered) SauceStatusTone.Success else SauceStatusTone.Danger,
                            showIndicator = isAnswered)
                    }
                    if (isAdminAnswer) SauceStatusBadge("Администратор", tone = SauceStatusTone.Success, showIndicator = false)
                    if (isReview) {
                        SauceStatusBadge("Отзыв", showIndicator = false)
                        averageScore?.takeIf { it.isFinite() && it > 0 }?.let {
                            SauceStatusBadge("★ ${((it.coerceAtMost(5.0) * 10).roundToInt() / 10.0)}", tone = SauceStatusTone.Warning, showIndicator = false)
                        }
                    }
                }
            }
            CompositionLocalProvider(LocalTextSelectionColors provides TextSelectionColors(colors.action, colors.actionSoft)) {
                SelectionContainer {
                    ClickableText(text = message, fontSize = 14.sp, lineHeight = 21.sp,
                        fontFamily = FontNunito.regular(), color = colors.textPrimary)
                }
            }
            if (!isReply) {
                Column {
                    FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                        verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (replies.isNotEmpty()) {
                            SauceButton(
                                title = if (!showRepliesButton) "Ответы · ${replies.size}" else if (showReply) "Скрыть ответы" else "Ответы · ${replies.size}",
                                variant = SauceButtonVariant.Text,
                                fillWidth = false,
                                enabled = actionsEnabled,
                                onClick = {
                                    if (onClickShowReplies != null) onClickShowReplies()
                                    else if (showRepliesButton) showReply = !showReply
                                    else onClickReply()
                                },
                            )
                        }
                        SauceButton("Ответить", onClickReply, variant = SauceButtonVariant.Text, enabled = actionsEnabled, fillWidth = false)
                    }
                    AnimatedVisibility(showReply && showRepliesButton) {
                        Column(Modifier.padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            replies.forEach { reply ->
                                StartCommentCard(
                                    userImageUrl = reply.user.photo.orEmpty(), userName = "${reply.user.surname} ${reply.user.name}".trim(),
                                    commentCreateDate = reply.createdAt, message = reply.comment, isReply = true,
                                    isAdminAnswer = reply.isAdminAnswer, onClickReply = {},
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyAvatar() {
    Box(Modifier.size(36.dp).background(SauceTheme.colors.surfaceSubtle, CircleShape), contentAlignment = Alignment.Center) {
        Icon(Icons.Default.PersonOutline, contentDescription = null,
            modifier = Modifier.size(20.dp), tint = SauceTheme.colors.textMuted)
    }
}
