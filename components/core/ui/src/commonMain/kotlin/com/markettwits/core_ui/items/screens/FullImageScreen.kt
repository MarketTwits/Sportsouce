package com.markettwits.core_ui.items.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
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
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import kotlin.math.roundToInt

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
        val rawIndex = selectedIndex ?: image.indexOf(selectedImageUrl)
        val initialIndex = if (rawIndex in 0 until image.size) rawIndex else 0
        val pagerState = rememberPagerState(initialPage = initialIndex, pageCount = { image.size })

        val scope = rememberCoroutineScope()
        val offsetY = remember { androidx.compose.animation.core.Animatable(0f) }
        val density = LocalDensity.current
        val dismissThresholdPx = with(density) { 140.dp.toPx() }
        val dismissVelocityPx = 1400f

        val bgAlpha = run {
            val progress = (kotlin.math.abs(offsetY.value) / (dismissThresholdPx * 2f)).coerceIn(0f, 1f)
            1f - progress
        }

        LaunchedEffect(pagerState.currentPage) {
            if (offsetY.value != 0f) offsetY.snapTo(0f)
        }

        val draggableState =
            androidx.compose.foundation.gestures.rememberDraggableState { delta ->
                scope.launch { offsetY.snapTo(offsetY.value + delta) }
            }

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background.copy(alpha = bgAlpha))
                .fillMaxSize()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset { androidx.compose.ui.unit.IntOffset(0, offsetY.value.roundToInt()) }
                        .draggable(
                            state = draggableState,
                            orientation = androidx.compose.foundation.gestures.Orientation.Vertical,
                            onDragStopped = { velocity ->
                                if (kotlin.math.abs(offsetY.value) >= dismissThresholdPx || kotlin.math.abs(velocity) >= dismissVelocityPx) {
                                    onDismiss()
                                } else {
                                    scope.launch {
                                        offsetY.animateTo(
                                            targetValue = 0f,
                                            animationSpec = androidx.compose.animation.core.spring(
                                                stiffness = androidx.compose.animation.core.Spring.StiffnessMediumLow
                                            )
                                        )
                                    }
                                }
                            }
                        )
                ) {
                    ImageItem(image = image[index])
                }
            }

            BackFloatingActionButton(
                modifier = Modifier
                    .alpha(bgAlpha)
                    .align(Alignment.TopStart),
                back = onDismiss
            )

            Row(
                modifier = Modifier
                    .alpha(bgAlpha)
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
                    text = buildAnnotatedString { append(" / ${pagerState.pageCount}") },
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
        val scope = rememberCoroutineScope()
        val offsetY = remember { androidx.compose.animation.core.Animatable(0f) }
        val density = LocalDensity.current
        val dismissThresholdPx = with(density) { 140.dp.toPx() }
        val dismissVelocityPx = 1400f

        val bgAlpha = run {
            val progress = (kotlin.math.abs(offsetY.value) / (dismissThresholdPx * 2f)).coerceIn(0f, 1f)
            1f - progress
        }

        val draggableState =
            androidx.compose.foundation.gestures.rememberDraggableState { delta ->
                scope.launch { offsetY.snapTo(offsetY.value + delta) }
            }

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background.copy(alpha = bgAlpha))
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { androidx.compose.ui.unit.IntOffset(0, offsetY.value.roundToInt()) }
                    .draggable(
                        state = draggableState,
                        orientation = androidx.compose.foundation.gestures.Orientation.Vertical,
                        onDragStopped = { velocity ->
                            if (kotlin.math.abs(offsetY.value) >= dismissThresholdPx || kotlin.math.abs(velocity) >= dismissVelocityPx) {
                                onDismiss()
                            } else {
                                scope.launch {
                                    offsetY.animateTo(
                                        targetValue = 0f,
                                        animationSpec = androidx.compose.animation.core.spring(
                                            stiffness = androidx.compose.animation.core.Spring.StiffnessMediumLow
                                        )
                                    )
                                }
                            }
                        }
                    )
            ) {
                ImageItem(
                    image = image,
                    painter = painter,
                )
            }

            BackFloatingActionButton(
                modifier = Modifier
                    .alpha(bgAlpha)
                    .align(Alignment.TopStart),
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

