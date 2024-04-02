package com.flipperdevices.settings.impl.composable.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMade
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun FlipperDialogContent(
    buttons: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    image: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    text: (@Composable () -> Unit)? = null,
    onDismissRequest: (() -> Unit)? = null
) = Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
    if (onDismissRequest != null) {
        Box(
            Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                modifier = Modifier
                    .padding(top = 12.dp, end = 12.dp, start = 12.dp)
                    .size(size = 24.dp)
                    .clickable(enabled = true, onClick = onDismissRequest),
                imageVector = Icons.AutoMirrored.Default.CallMade,
                contentDescription = ""
            )
        }
    }
    if (image != null) {
        Box(modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp)) {
            image()
        }
    }
    if (title != null) {
        Box(modifier = Modifier.padding(top = 24.dp, start = 12.dp, end = 12.dp)) {
            title()
        }
    }
    if (text != null) {
        Box(modifier = Modifier.padding(top = 4.dp, start = 12.dp, end = 12.dp)) {
            text()
        }
    }

    Box(
        modifier = Modifier.padding(
            top = 24.dp,
            start = 12.dp,
            end = 12.dp,
            bottom = 12.dp
        )
    ) {
        buttons()
    }
}
