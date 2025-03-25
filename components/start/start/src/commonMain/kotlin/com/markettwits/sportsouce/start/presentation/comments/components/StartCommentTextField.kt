package com.markettwits.sportsouce.start.presentation.comments.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.start.presentation.start.component.CommentMode

@Composable
internal fun StartCommentTextField(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    mode: CommentMode,
    onClickCloseReply: () -> Unit,
    sendComment: (String) -> Unit
) {
    var comment by rememberSaveable {
        mutableStateOf("")
    }

    val focusRequester = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(mode) {
        if (mode is CommentMode.Reply) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
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
                    Spacer(modifier = Modifier.size(8.dp))
                    Icon(
                        modifier = Modifier
                            .size(18.dp)
                            .clickable {
                                onClickCloseReply()
                                keyboardController?.hide()
                            },
                        imageVector = Icons.Default.Clear,
                        contentDescription = "rule",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
    TextField(
        modifier = Modifier
            .focusRequester(focusRequester)
            .fillMaxWidth(),
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