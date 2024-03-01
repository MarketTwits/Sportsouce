package com.markettwits.core_ui.base_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.R
import com.markettwits.core_ui.components.BackFloatingActionButton
import com.markettwits.core_ui.image.request.imageRequestCrossfade
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
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            SubcomposeAsyncImage(
                model = imageRequestCrossfade(image),
                filterQuality = FilterQuality.Medium,
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .zoomable(zoomState = zoomState),
                error = {
                    if (image.isEmpty())
                        SubcomposeAsyncImageContent(
                            modifier = Modifier,
                            painter = painterResource(id = R.drawable.default_start_image)
                        )
                    else
                        Box(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer))
                },
                loading = {
                    Box(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer))
                },
                success = {
                    zoomState.setContentSize(it.painter.intrinsicSize)
                    SubcomposeAsyncImageContent(modifier = Modifier)
                }
            )
            BackFloatingActionButton(
                modifier = Modifier.align(Alignment.TopStart),
                back = onDismiss
            )
        }
    }
}