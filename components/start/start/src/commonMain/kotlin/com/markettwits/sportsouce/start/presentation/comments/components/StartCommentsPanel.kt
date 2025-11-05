package com.markettwits.sportsouce.start.presentation.comments.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.markettwits.core_ui.items.components.progress.shimmer
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Комментарии",
                fontSize = 18.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.onPrimary
            )

            if (comments.rows.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .clip(Shapes.small)
                        .clickable { onClickViewAll() }
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Показать все (${comments.rows.size})",
                        fontSize = 14.sp,
                        fontFamily = FontNunito.semiBoldBold(),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            if (comments.rows.isNotEmpty()) {
                val displayComments = comments.rows.take(maxComments)
                displayComments.forEach {
                    StartCommentCard(
                        modifier = Modifier,
                        userImageUrl = it.user.photo ?: "",
                        userName = "${it.user.surname} ${it.user.name}",
                        commentCreateDate = it.createdAt,
                        message = it.comment,
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
) {
    var showReply by rememberSaveable {
        mutableStateOf(false)
    }

    var isShowAvatarDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if (isShowAvatarDialog) {
        FullImageScreen(image = userImageUrl) {
            isShowAvatarDialog = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row {
            Box {
                if (userImageUrl.isNotEmpty())
                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .clip(Shapes.large)
                            .size(36.dp)
                            .clickable { isShowAvatarDialog = true },
                        model = userImageUrl,
                        contentDescription = userName,
                        contentScale = ContentScale.Crop,
                        loading = {
                            LoadingAvatar(size = 36.dp)
                        },
                        error = {
                            EmptyAvatar(userName = userName, size = 36.dp)
                        }
                    )
                else
                    EmptyAvatar(userName = userName, size = 36.dp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = userName,
                        fontSize = 15.sp,
                        fontFamily = FontNunito.bold(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = commentCreateDate,
                        fontSize = 13.sp,
                        fontFamily = FontNunito.regular(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                val selectionColor = TextSelectionColors(
                    handleColor = MaterialTheme.colorScheme.tertiary,
                    backgroundColor = MaterialTheme.colorScheme.tertiaryContainer
                )
                CompositionLocalProvider(LocalTextSelectionColors provides selectionColor) {
                    SelectionContainer {
                        ClickableText(
                            text = message,
                            fontSize = 15.sp,
                            fontFamily = FontNunito.regular(),
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!isReply) {
                            Box(
                                modifier = Modifier
                                    .clip(Shapes.small)
                                    .clickable { onClickReply() }
                                    .padding(horizontal = 4.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Ответить",
                                    fontSize = 13.sp,
                                    fontFamily = FontNunito.semiBoldBold(),
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }

                        if (replies.isNotEmpty() && !showRepliesButton) {
                            Text(
                                text = "${replies.size} ${if (replies.size == 1) "ответ" else if (replies.size < 5) "ответа" else "ответов"}",
                                fontSize = 13.sp,
                                fontFamily = FontNunito.regular(),
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }

                    if (!isReply && replies.isNotEmpty() && showRepliesButton && !showReply) {
                        Box(
                            modifier = Modifier
                                .clip(Shapes.small)
                                .clickable {
                                    if (onClickShowReplies != null) {
                                        onClickShowReplies()
                                    } else {
                                        showReply = !showReply
                                    }
                                }
                                .padding(horizontal = 4.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Показать ${replies.size} ${if (replies.size == 1) "ответ" else if (replies.size < 5) "ответа" else "ответов"}",
                                fontSize = 14.sp,
                                fontFamily = FontNunito.bold(),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }

                if (!isReply && replies.isNotEmpty() && showRepliesButton) {

                    AnimatedVisibility(
                        visible = showReply,
                        enter = expandVertically(animationSpec = tween(durationMillis = 300)) + fadeIn(
                            animationSpec = tween(durationMillis = 300)
                        ),
                        exit = shrinkVertically(animationSpec = tween(durationMillis = 300)) + fadeOut(
                            animationSpec = tween(durationMillis = 300)
                        )
                    ) {
                        Column(modifier = Modifier.padding(start = 12.dp, top = 8.dp)) {
                            replies.forEach {
                                StartCommentCard(
                                    modifier = Modifier,
                                    isReply = true,
                                    userName = "${it.user.surname} ${it.user.name}",
                                    commentCreateDate = it.createdAt,
                                    message = it.comment,
                                    userImageUrl = it.user.photo ?: "",
                                    onClickReply = {}
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .clip(Shapes.small)
                                    .clickable { showReply = !showReply }
                                    .padding(horizontal = 4.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Скрыть",
                                    fontSize = 14.sp,
                                    fontFamily = FontNunito.bold(),
                                    color = MaterialTheme.colorScheme.secondary
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
private fun EmptyAvatar(
    modifier: Modifier = Modifier,
    userName: String,
    size: androidx.compose.ui.unit.Dp = 40.dp,
) {
    Box(
        modifier = modifier
            .clip(Shapes.large)
            .size(size)
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.6f))
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = userName.firstOrNull()?.uppercaseChar()?.toString() ?: "",
            fontSize = (size.value * 0.45f).sp,
            fontFamily = FontNunito.semiBoldBold(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onTertiary
        )
    }
}

@Composable
private fun LoadingAvatar(
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 40.dp,
) {
    Box(
        modifier = modifier
            .clip(Shapes.large)
            .size(size)
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.6f))
            .shimmer(
                tiltAngle = 30,
                gradientColors = listOf(
                    Color.Transparent,
                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f),
                    Color.Transparent,
                )
            )
    ) {}
}

