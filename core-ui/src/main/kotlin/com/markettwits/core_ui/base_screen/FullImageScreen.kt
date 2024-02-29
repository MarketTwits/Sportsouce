package com.markettwits.core_ui.base_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import com.markettwits.core_ui.components.BackFloatingActionButton
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@Composable
fun FullImageScreen(
    image: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val zoomState = rememberZoomState(maxScale = 25f)
        val painter = rememberAsyncImagePainter(
            model = image,
            contentScale = ContentScale.Fit,
            onSuccess = {
                zoomState.setContentSize(it.painter.intrinsicSize)
            }
        )
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .zoomable(zoomState = zoomState),
                painter = painter,
                contentScale = ContentScale.Fit,
                contentDescription = ""
            )
            BackFloatingActionButton(
                modifier = Modifier.align(Alignment.TopStart),
                back = onDismiss
            )
        }
    }
}