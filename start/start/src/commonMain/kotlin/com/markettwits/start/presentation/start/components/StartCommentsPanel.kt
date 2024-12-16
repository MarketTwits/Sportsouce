package com.markettwits.start.presentation.start.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.markettwits.core_ui.items.base_screen.FullImageScreen
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.domain.StartItem
import com.markettwits.start.presentation.start.component.CommentMode


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
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
private fun StartCommentCard(
    modifier: Modifier = Modifier,
    userImageUrl : String,
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

    var isShowAvatarDialog by rememberSaveable{
        mutableStateOf(false)
    }

    if (isShowAvatarDialog){
        FullImageScreen(image = userImageUrl){
            isShowAvatarDialog = false
        }
    }

    Row(modifier = modifier.padding(vertical = 10.dp)) {
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
            Box(modifier = Modifier
                .clip(Shapes.large)
                .size(40.dp)
                .background(Color.Gray)
            )
        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
        Column(modifier = modifier) {
            Row {
                Text(
                    text = userName,
                    fontSize = 14.sp,
                    fontFamily = FontNunito.semiBoldBold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                Text(
                    text = commentCreateDate,
                    fontSize = 10.sp,
                    fontFamily = FontNunito.medium(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Text(
                text = message,
                fontSize = 12.sp,
                fontFamily = FontNunito.medium(),
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.tertiary
            )
            if (!isReply) {
                Text(
                    modifier = Modifier.clickable {
                        onClickReply()
                    },
                    text = "Ответить",
                    fontSize = 12.sp,
                    fontFamily = FontNunito.bold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary
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
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
                AnimatedVisibility(
                    visible = showReply,
                    enter = expandVertically(animationSpec = tween(durationMillis = 300)) + fadeIn(animationSpec = tween(durationMillis = 300)),
                    exit = shrinkVertically(animationSpec = tween(durationMillis = 300)) + fadeOut(animationSpec = tween(durationMillis = 300))
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
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CommentTextField(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    mode: CommentMode,
    onClickCloseReply: () -> Unit,
    sendComment: (String) -> Unit
) {
    var comment by rememberSaveable {
        mutableStateOf("")
    }

    Box(
        Modifier
            .fillMaxWidth()
            .shadow(elevation = 20.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(3.dp),
    ) {
        if (mode is CommentMode.Reply) {
            Row(
                modifier = modifier
                    .align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Ответ для ${mode.replier}",
                        fontSize = 12.sp,
                        fontFamily = FontNunito.bold(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                    Icon(
                        modifier = Modifier
                            .size(15.dp)
                            .clickable {
                                onClickCloseReply()
                            },
                        imageVector = Icons.Default.Clear,
                        contentDescription = "rule",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }


            }

        }
        Row(
            modifier
                .align(Alignment.CenterEnd)
                .padding(end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(12.dp),
                imageVector = Icons.Default.Book,
                contentDescription = "rule",
                tint = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = "Правила",
                fontSize = 12.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.tertiary
            )
        }

    }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = comment,
        maxLines = 3,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
            focusedContainerColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.tertiary,
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.outline
        ),
        placeholder = {
            Text(
                text = "Ваш комментарий",
                fontSize = 14.sp,
                fontFamily = FontNunito.regular(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray
            )
        },
        trailingIcon = {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                )
            } else {
                IconButton(
                    enabled = !isLoading,
                    onClick = {
                        sendComment(comment)
                    }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "send comment",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        },
        onValueChange = {
            comment = it
        })
}