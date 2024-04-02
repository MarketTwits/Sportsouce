package com.flipperdevices.settings.impl.composable.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun FlipperDialog(
    buttons: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    image: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    text: (@Composable () -> Unit)? = null,
    onDismissRequest: (() -> Unit)? = null,
    closeOnClickOutside: Boolean = true
) {
    // Disable selection on dialog, because on SelectionContainer crash
    DisableSelection {
        Dialog(onDismissRequest = {
            if (closeOnClickOutside) {
                onDismissRequest?.invoke()
            }
        }) {
            Box(
                modifier = modifier
                    .clip(RoundedCornerShape(18.dp))
            ) {
                FlipperDialogContent(
                    buttons,
                    image = image,
                    title = title,
                    text = text,
                    onDismissRequest = onDismissRequest
                )
            }
        }
    }
}

@Composable
fun FlipperDialog(
    @StringRes buttonTextId: Int,
    onClickButton: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes imageId: Int? = null,
    @StringRes titleId: Int? = null,
    imageComposable: (@Composable () -> Unit)? =
        {
            Image(
                painter = painterResource(imageId ?: 0),
                contentDescription = titleId?.let { stringResource(it) }
            )
        },
    titleComposable: (@Composable () -> Unit)? = {
        Text(
            text = "text",
            textAlign = TextAlign.Center
        )
    },
    @StringRes textId: Int? = null,
    textComposable: (@Composable () -> Unit)? = {
        Text(
            text = stringResource(textId ?: 0),
            textAlign = TextAlign.Center
        )
    },
    buttonComposable: @Composable () -> Unit = {
        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClickButton,
        ) {
            Text(text = stringResource(buttonTextId))
        }

    },
    onDismissRequest: (() -> Unit)? = null,
    closeOnClickOutside: Boolean = true
) {
    FlipperDialog(
        buttonComposable,
        modifier,
        image = imageComposable,
        title = titleComposable,
        text = textComposable,
        onDismissRequest,
        closeOnClickOutside
    )
}
