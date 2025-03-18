package com.markettwits.core_ui.items.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.buttons.BackFloatingActionButton
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.theme.FontNunito
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@Composable
fun FullImageScreen(
    image: Painter,
    onDismiss: () -> Unit
) {
    FullImageScreenInternal(painter = image, onDismiss = onDismiss)
}

@Composable
fun FullImageScreen(
    image: String,
    onDismiss: () -> Unit
) {
    FullImageScreenInternal(image = image, onDismiss = onDismiss)
}

@Composable
fun FullImageScreen(
    image: List<String>,
    selectedImageUrl: String,
    onDismiss: () -> Unit
) {
    if (image.isNotEmpty()) {
        FullImageScreenInternal(image = image, selectedImageUrl = selectedImageUrl, onDismiss = onDismiss)
    }
}

@Composable
fun FullImageScreen(
    image: List<String>,
    selectedImageIndex: Int,
    onDismiss: () -> Unit
) {
    if (image.isNotEmpty()) {
        FullImageScreenInternal(image = image, selectedIndex = selectedImageIndex, onDismiss = onDismiss)
    }
}

@Composable
fun FullImageScreenInternal(
    image: List<String>,
    selectedIndex: Int? = null,
    selectedImageUrl: String? = null,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val selectedImageIndex = selectedIndex ?: image.indexOf(selectedImageUrl)
        val pagerState = rememberPagerState(initialPage = selectedImageIndex, pageCount = { image.size })

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            HorizontalPager(
                state = pagerState
            ) { index ->
                ImageItem(image = image[index])
            }
            BackFloatingActionButton(
                modifier = Modifier.align(Alignment.TopStart),
                back = onDismiss
            )
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.BottomCenter),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = (pagerState.targetPage + 1).toString(),
                    color = MaterialTheme.colorScheme.tertiary,
                    fontFamily = FontNunito.bold(),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
                Text(
                    text = buildAnnotatedString {
                        append(" / ${pagerState.pageCount}")
                    },
                    fontFamily = FontNunito.medium(),
                    color = MaterialTheme.colorScheme.tertiary,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
        }
    }
}


@Composable
internal fun FullImageScreenInternal(
    image: String? = null,
    painter: Painter? = null,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            ImageItem(
                image = image,
                painter = painter,
            )
            BackFloatingActionButton(
                modifier = Modifier.align(Alignment.TopStart),
                back = onDismiss
            )
        }
    }
}

@Composable
internal fun ImageItem(
    image: String? = null,
    painter: Painter? = null,
) {
    val zoomState = rememberZoomState(maxScale = 25f)
    SubcomposeAsyncImage(
        model = image ?: painter,
        filterQuality = FilterQuality.High,
        contentDescription = "",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxSize()
            .zoomable(zoomState = zoomState),
        error = {
            Box(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer))
        },
        loading = {
            Box(
                modifier = Modifier
                    .shimmer(tiltAngle = 30)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            )
        },
        success = {
            zoomState.setContentSize(it.painter.intrinsicSize)
            SubcomposeAsyncImageContent(modifier = Modifier)
        }
    )
}

