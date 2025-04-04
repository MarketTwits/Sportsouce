package com.markettwits.sportsouce.start.presentation.comments.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.markettwits.core_ui.items.screens.FullImageScreen
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.start.domain.StartItem
import com.markettwits.sportsouce.start.presentation.start.components.StartContentBasePanel


@Composable
internal fun StartCommentsPanel(
    modifier: Modifier = Modifier,
    comments: StartItem.Comments,
    onClickReply: (String, Int) -> Unit,
) {
    StartContentBasePanel(
        modifier = modifier,
        label = "Комментарии"
    ) {
        Column {
            if (comments.rows.isNotEmpty()) {
                comments.rows.forEach {
                    StartCommentCard(
                        modifier = modifier,
                        userImageUrl = it.user.photo ?: "",
                        userName = "${it.user.surname} ${it.user.name}",
                        commentCreateDate = it.createdAt,
                        message = it.comment,
                        onClickReply = {
                            onClickReply(it.user.name, it.id)
                        },
                        replies = it.replies
                    )
                }
            } else {
                Text(
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    text = "Комментариев пока нет",
                    fontSize = 15.sp,
                    fontFamily = FontNunito.medium(),
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun StartCommentCard(
    modifier: Modifier = Modifier,
    userImageUrl: String,
    userName: String,
    commentCreateDate: String,
    message: String,
    replies: List<StartItem.Comments.Reply> = emptyList(),
    onClickReply: () -> Unit,
    isReply: Boolean = false
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

    Row(
        modifier = modifier.padding(
            vertical = 10.dp,
            horizontal = 5.dp,
        )
    ) {
        if (userImageUrl.isNotEmpty())
            AsyncImage(
                modifier = Modifier
                    .clip(Shapes.large)
                    .size(40.dp)
                    .clickable { isShowAvatarDialog = true },
                model = userImageUrl,
                contentDescription = userName,
                contentScale = ContentScale.Crop
            )
        else
            Box(
                modifier = Modifier
                    .clip(Shapes.large)
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.6f))
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = userName.firstOrNull()?.uppercaseChar()?.toString() ?: "",
                    fontSize = 18.sp,
                    fontFamily = FontNunito.semiBoldBold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        Column(modifier = modifier) {
            Row {
                Text(
                    text = userName,
                    fontSize = 14.sp,
                    fontFamily = FontNunito.semiBoldBold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                Text(
                    text = commentCreateDate,
                    fontSize = 10.sp,
                    fontFamily = FontNunito.regular(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Text(
                text = message,
                fontSize = 14.sp,
                fontFamily = FontNunito.medium(),
                maxLines = 5,
                lineHeight = 16.sp,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimary
            )
            if (!isReply) {
                Text(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(Shapes.small)
                        .clickable {
                            onClickReply()
                        },
                    text = "Ответить",
                    fontSize = 12.sp,
                    fontFamily = FontNunito.bold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            if (replies.isNotEmpty()) {
                if (!showReply) {
                    Text(
                        modifier = Modifier.clickable {
                            showReply = !showReply
                        },
                        text = "Показать ${replies.size} ответов",
                        fontSize = 12.sp,
                        fontFamily = FontNunito.bold(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                AnimatedVisibility(
                    visible = showReply,
                    enter = expandVertically(animationSpec = tween(durationMillis = 300)) + fadeIn(
                        animationSpec = tween(
                            durationMillis = 300
                        )
                    ),
                    exit = shrinkVertically(animationSpec = tween(durationMillis = 300)) + fadeOut(
                        animationSpec = tween(
                            durationMillis = 300
                        )
                    )
                ) {
                    Column {
                        replies.forEach {
                            StartCommentCard(
                                modifier = modifier,
                                userName = "${it.user.surname} ${it.user.name}",
                                commentCreateDate = it.createdAt,
                                message = it.comment,
                                userImageUrl = it.user.photo ?: "",
                                isReply = true,
                                onClickReply = {}
                            )
                        }
                        Text(
                            modifier = Modifier.clickable {
                                showReply = !showReply
                            },
                            text = "Скрыть",
                            fontSize = 12.sp,
                            fontFamily = FontNunito.bold(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}

