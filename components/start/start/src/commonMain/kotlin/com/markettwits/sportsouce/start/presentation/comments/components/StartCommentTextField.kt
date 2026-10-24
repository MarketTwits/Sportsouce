package com.markettwits.sportsouce.start.presentation.comments.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.textField.BoundlessTextFieldBase
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSizes
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.presentation.start.component.CommentMode

@Composable
internal fun StartCommentTextField(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    comment: String = "",
    onCommentChanged: (String) -> Unit = {},
    mode: CommentMode,
    readOnly: Boolean = false,
    onClickTextField: () -> Unit = {},
    onClickCloseReply: () -> Unit,
    sendComment: (String) -> Unit,
) {
    val colors = SauceTheme.colors
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    val windowFocused = LocalWindowInfo.current.isWindowFocused
    var editorFocused by remember { mutableStateOf(false) }
    val canSend = !readOnly && !isLoading && comment.isNotBlank()
    val submit = { if (canSend) sendComment(comment) }

    LaunchedEffect(mode, readOnly, windowFocused) {
        if (mode is CommentMode.Reply && !readOnly && windowFocused) {
            withFrameNanos { }
            focusRequester.requestFocus()
        }
    }
    LaunchedEffect(mode, readOnly, windowFocused, editorFocused) {
        if (mode is CommentMode.Reply && !readOnly && windowFocused && editorFocused) {
            withFrameNanos { }
            keyboard?.show()
        }
    }

    Column(
        modifier = modifier.padding(horizontal = SauceSpacing.large, vertical = SauceSpacing.small),
        verticalArrangement = Arrangement.spacedBy(SauceSpacing.small),
    ) {
        if (mode is CommentMode.Reply) {
            Surface(color = colors.surfaceSubtle, shape = SauceShapes.field) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Ответ: ${mode.replier}",
                        modifier = Modifier.weight(1f).padding(start = SauceSpacing.medium),
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = colors.textSecondary,
                    )
                    IconButton(
                        modifier = Modifier.size(SauceSizes.touchTarget).clip(SauceShapes.button),
                        enabled = !isLoading,
                        onClick = { keyboard?.hide(); onClickCloseReply() },
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Отменить ответ",
                            tint = colors.textMuted, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
        BoundlessTextFieldBase(
            modifier = Modifier.fillMaxWidth()
                .heightIn(min = SauceSizes.fieldMinHeight)
                .clip(SauceShapes.field)
                .border(1.dp, if (editorFocused && !isLoading) colors.action else colors.border, SauceShapes.field)
                .focusRequester(focusRequester)
                .onFocusChanged { editorFocused = it.isFocused }
                .onPreviewKeyEvent { event ->
                    if (!readOnly && event.key == Key.Enter && (event.isCtrlPressed || event.isMetaPressed)) {
                        if (event.type == KeyEventType.KeyDown) submit()
                        true
                    } else false
                }
                .semantics {
                    contentDescription = if (readOnly) "Написать комментарий" else "Комментарий"
                    if (isLoading) stateDescription = "Отправка комментария"
                },
            value = comment,
            onValueChange = { if (!readOnly && !isLoading) onCommentChanged(it) },
            isEnabled = !isLoading,
            readOnly = readOnly,
            onClick = if (readOnly) onClickTextField else null,
            singleLine = false,
            minLines = 1,
            maxLines = 4,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, imeAction = ImeAction.Default),
            keyboardActions = KeyboardActions(onSend = { submit() }),
            placeholder = {
                Text(if (mode is CommentMode.Reply) "Напишите ответ…" else "Напишите комментарий…",
                    style = MaterialTheme.typography.bodyLarge, color = colors.textMuted,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
            },
            trailingIcon = if (readOnly) null else {
                {
                    IconButton(
                        onClick = submit,
                        enabled = canSend,
                        modifier = Modifier.padding(4.dp).size(SauceSizes.touchTarget).clip(SauceShapes.button)
                            .semantics { contentDescription = "Отправить комментарий" },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = colors.actionSoft,
                            contentColor = colors.action,
                            disabledContainerColor = if (isLoading) colors.actionSoft else Color.Transparent,
                            disabledContentColor = colors.disabledContent,
                        ),
                    ) {
                        if (isLoading) CircularProgressIndicator(Modifier.size(20.dp), color = colors.action, strokeWidth = 2.dp)
                        else Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null,
                            modifier = Modifier.size(SauceSizes.icon))
                    }
                }
            },
        )
    }
}
