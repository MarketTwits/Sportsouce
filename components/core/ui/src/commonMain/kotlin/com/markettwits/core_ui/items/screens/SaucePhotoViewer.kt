package com.markettwits.core_ui.items.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ZoomOutMap
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.unit.*
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.image.AlbumImage
import com.markettwits.core_ui.items.image.AlbumImageSize
import com.markettwits.core_ui.items.theme.*
import dev.chrisbanes.haze.HazeState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.*
import kotlin.math.abs

/** A read-only photo gallery. The caller owns paging and restores the grid to the returned index. */
@Composable
fun SaucePhotoViewer(
    images: List<String>,
    initialIndex: Int,
    title: String,
    totalCount: Int = images.size,
    appendError: Boolean = false,
    onRetry: () -> Unit = {},
    onPageChanged: (Int) -> Unit = {},
    transitionSource: PhotoTransitionSource? = null,
    prepareReturn: suspend (Int) -> PhotoTransitionSource? = { null },
    onShare: ((Int) -> Unit)? = null,
    onDownload: ((Int) -> Unit)? = null,
    onDismiss: (Int) -> Unit,
) {
    if (images.isEmpty()) return
    var closeRequest by remember { mutableIntStateOf(0) }
    val backdrop = photoViewerBackdropState()
    SauceTheme(darkTheme = true) {
        PhotoViewerOverlay(onDismissRequest = { closeRequest++ }) {
            PhotoViewerContent(images, initialIndex, title, totalCount, appendError, onRetry,
                onPageChanged = onPageChanged, onDismiss = onDismiss, transitionSource = transitionSource,
                prepareReturn = prepareReturn, closeRequest = closeRequest, backdrop = backdrop, onShare = onShare, onDownload = onDownload)
        }
    }
}

@Composable
internal fun PhotoViewerContent(
    images: List<String>,
    initialIndex: Int,
    title: String,
    totalCount: Int,
    appendError: Boolean,
    onRetry: () -> Unit,
    onPageChanged: (Int) -> Unit,
    onDismiss: (Int) -> Unit,
    transitionSource: PhotoTransitionSource? = null,
    prepareReturn: suspend (Int) -> PhotoTransitionSource? = { null },
    closeRequest: Int = 0,
    backdrop: HazeState? = null,
    onShare: ((Int) -> Unit)? = null,
    onDownload: ((Int) -> Unit)? = null,
) {
    val colors = SauceTheme.colors
    val scope = rememberCoroutineScope()
    var previewIndex by remember { mutableStateOf<Int?>(null) }
    var previewPainter by remember { mutableStateOf<Pair<Int, Painter>?>(null) }
    var navigationJob by remember { mutableStateOf<Job?>(null) }
    val pager = rememberPagerState(initialPage = initialIndex.coerceIn(images.indices), pageCount = { images.size })
    val displayedIndex = (previewIndex ?: pager.currentPage).coerceIn(images.indices)
    val actionsEnabled = previewIndex == null && !pager.isScrollInProgress
    var controlsVisible by rememberSaveable { mutableStateOf(true) }
    var closing by remember { mutableStateOf(false) }
    var activeZoom by remember { mutableStateOf<ZoomState?>(null) }
    val zoomed by remember { derivedStateOf { (activeZoom?.scale ?: 1f) > 1.01f } }
    val entry = remember { Animatable(0f) }
    var sharedSource by remember { mutableStateOf(transitionSource) }
    var currentPainter by remember { mutableStateOf(transitionSource?.painter?.let { initialIndex to it }) }
    var windowOrigin by remember { mutableStateOf(Offset.Zero) }
    var sharedClosing by remember { mutableStateOf(false) }
    var sharedEndOffset by remember { mutableStateOf(Offset.Zero) }
    var sharedEndScale by remember { mutableFloatStateOf(1f) }
    val prepare by rememberUpdatedState(prepareReturn)
    var dragOffset by remember { mutableFloatStateOf(0f) }
    val springBack = remember { Animatable(0f) }
    val density = LocalDensity.current
    val threshold = with(density) { 140.dp.toPx() }
    val dismiss by rememberUpdatedState(onDismiss)
    val pageChanged by rememberUpdatedState(onPageChanged)
    val close: () -> Unit = {
        if (!closing) {
            val closingIndex = displayedIndex
            closing = true
            navigationJob?.cancel()
            scope.launch {
                if (pager.isScrollInProgress || pager.currentPage != closingIndex) pager.scrollToPage(closingIndex)
                previewIndex = null
                val source = prepare(closingIndex)
                if (source != null) {
                    activeZoom?.reset()
                    sharedSource = source.copy(painter = previewPainter?.takeIf { it.first == closingIndex }?.second
                        ?: currentPainter?.takeIf { it.first == closingIndex }?.second ?: source.painter)
                    sharedEndOffset = Offset(0f, dragOffset)
                    sharedEndScale = 1f - (abs(dragOffset) / (threshold * 2)).coerceIn(0f, 1f) * .12f
                    sharedClosing = true
                }
                entry.animateTo(0f, tween(if (source != null) 280 else 160))
                dismiss(closingIndex)
            }
        }
    }
    val dragProgress = (abs(dragOffset) / (threshold * 2)).coerceIn(0f, 1f)
    LaunchedEffect(Unit) { entry.animateTo(1f, tween(if (transitionSource != null) 280 else 180)) }
    LaunchedEffect(closeRequest) { if (closeRequest > 0) close() }
    val haptic = LocalHapticFeedback.current
    var previousIndex by remember { mutableIntStateOf(displayedIndex) }
    LaunchedEffect(displayedIndex) {
        dragOffset = 0f
        pageChanged(displayedIndex)
        if (previousIndex != displayedIndex) {
            previousIndex = displayedIndex
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }
    PhotoViewerSystemBars(controlsVisible)
    Box(Modifier.fillMaxSize().onGloballyPositioned { windowOrigin = it.positionInWindow() }) {
        PhotoViewerBackdropLayer(backdrop, entry.value * (1f - dragProgress), Modifier.matchParentSize())
        HorizontalPager(state = pager, key = { it }, userScrollEnabled = !zoomed && previewIndex == null && !closing && entry.value == 1f,
            beyondViewportPageCount = 1,
            modifier = Modifier.fillMaxSize().graphicsLayer {
                alpha = if (previewIndex != null || (sharedSource != null && (entry.value < 1f || sharedClosing))) 0f else entry.value
                translationY = dragOffset
                scaleX = (0.96f + entry.value * .04f) * (1f - dragProgress * .12f)
                scaleY = scaleX
            }.draggable(rememberDraggableState { delta -> dragOffset += delta }, Orientation.Vertical,
                enabled = !zoomed && previewIndex == null && !pager.isScrollInProgress && !closing && entry.value == 1f,
                onDragStopped = { velocity ->
                    if (abs(dragOffset) > threshold || (abs(velocity) > 1600f && abs(dragOffset) > threshold / 5)) close()
                    else {
                        springBack.snapTo(dragOffset)
                        springBack.animateTo(0f, spring()) { dragOffset = value }
                    }
                })) { index ->
            val zoom = rememberZoomState(maxScale = 8f)
            SideEffect { if (index == pager.currentPage) activeZoom = zoom }
            LaunchedEffect(pager.currentPage) { if (index != pager.currentPage) zoom.reset() }
            ViewerImage(images[index], zoom, index == pager.settledPage,
                previewPainter?.takeIf { it.first == index }?.second ?: sharedSource?.painter?.takeIf { index == initialIndex },
                { controlsVisible = !controlsVisible }) { painter ->
                if (index == pager.currentPage) currentPainter = index to painter
            }
        }
        previewIndex?.takeIf { it in images.indices }?.let { index ->
            AlbumImage(images[index], AlbumImageSize.Grid, Modifier.fillMaxSize().semantics {
                contentDescription = "Предпросмотр фотографии"
                stateDescription = "Фото ${index + 1}"
            }, contentScale = ContentScale.Fit, loading = {}, error = { _, _ -> },
                onPainter = { previewPainter = index to it })
        }
        sharedSource?.let { source ->
            if (entry.value < 1f || sharedClosing)
                PhotoSharedTransition(source, entry.value, windowOrigin, Modifier.fillMaxSize(), sharedEndOffset, sharedEndScale)
        }
        AnimatedVisibility(visible = controlsVisible && entry.value == 1f && !closing && abs(dragOffset) < threshold / 4,
            enter = fadeIn(tween(150)), exit = fadeOut(tween(150)), modifier = Modifier.align(Alignment.TopCenter)) {
            Row(Modifier.fillMaxWidth()
                .background(Brush.verticalGradient(listOf(colors.background.copy(alpha = .72f), colors.background.copy(alpha = 0f))))
                .statusBarsPadding().padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = close) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Закрыть фото", tint = colors.textPrimary) }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(title, style = MaterialTheme.typography.titleSmall, color = colors.textPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("${displayedIndex + 1} / $totalCount", style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
                }
                if (zoomed) IconButton(onClick = { scope.launch { activeZoom?.reset() } }) {
                    Icon(Icons.Outlined.ZoomOutMap, "Сбросить масштаб", tint = colors.textPrimary)
                }
                if (onDownload != null) IconButton(onClick = { onDownload(pager.settledPage) }, enabled = actionsEnabled) {
                    Icon(Icons.Outlined.Download, "Скачать оригинал", tint = colors.textPrimary)
                }
                if (onShare != null) IconButton(onClick = { onShare(pager.settledPage) }, enabled = actionsEnabled) {
                    Icon(Icons.Outlined.Share, "Поделиться оригиналом", tint = colors.textPrimary)
                }
            }
        }
        AnimatedVisibility(visible = controlsVisible && entry.value == 1f && !closing && abs(dragOffset) < threshold / 4,
            enter = fadeIn(tween(150)), exit = fadeOut(tween(150)), modifier = Modifier.align(Alignment.BottomCenter)) {
            Column(Modifier.fillMaxWidth()
                .background(Brush.verticalGradient(listOf(colors.background.copy(alpha = 0f), colors.background.copy(alpha = .72f))))
                .navigationBarsPadding().padding(top = 16.dp, bottom = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                if (appendError && pager.currentPage >= images.lastIndex - 5)
                    SauceButton("Повторить загрузку", onRetry, variant = SauceButtonVariant.Text, fillWidth = false)
                PhotoFilmstrip(images, displayedIndex,
                    position = { previewIndex?.toFloat() ?: (pager.currentPage + pager.currentPageOffsetFraction) },
                    onPreview = { index ->
                        if (!closing) {
                            navigationJob?.cancel()
                            previewIndex = index
                        }
                    }, onSelect = { index ->
                        if (!closing) {
                            navigationJob?.cancel()
                            previewIndex = index
                            navigationJob = scope.launch {
                                activeZoom?.reset()
                                pager.scrollToPage(index)
                                // Keep the cached preview until the destination page has painted.
                                withFrameNanos { }; withFrameNanos { }
                                previewIndex = null
                            }
                        }
                    })
            }
        }
    }
}

@Composable
private fun ViewerImage(url: String, zoom: ZoomState, active: Boolean, initialPainter: Painter?, onToggleControls: () -> Unit, onPainter: (Painter) -> Unit) {
    var opened by remember(url) { mutableStateOf(active) }
    LaunchedEffect(active) { if (active) opened = true }
    val scope = rememberCoroutineScope()
    val colors = SauceTheme.colors
    AlbumImage(url, if (active || opened) AlbumImageSize.Full else AlbumImageSize.Grid,
        contentScale = ContentScale.Fit, initialPainter = initialPainter, loadOriginal = active,
        modifier = Modifier.fillMaxSize().zoomable(zoom,
            scrollGesturePropagation = ScrollGesturePropagation.NotZoomed, onTap = { onToggleControls() })
            .semantics {
                contentDescription = "Просмотр фотографии"
                stateDescription = "Масштаб ${(zoom.scale * 100).toInt()}%"
                onClick(label = "Показать или скрыть панели") { onToggleControls(); true }
                customActions = listOf(CustomAccessibilityAction("Увеличить или уменьшить") {
                    scope.launch { zoom.toggleScale(2.5f, Offset.Zero) }; true
                })
            },
        loading = { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            SauceSkeleton(Modifier.fillMaxWidth(.65f).aspectRatio(1f))
        } },
        error = { hasPreview, retry -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(Modifier.padding(24.dp).clip(SauceShapes.card).background(colors.background.copy(alpha = .88f)).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(if (hasPreview) "Не удалось загрузить фото в высоком качестве" else "Не удалось загрузить фото", style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary)
                SauceButton("Повторить", retry, variant = SauceButtonVariant.Outlined, fillWidth = false)
            }
        } },
        onPainter = {
            onPainter(it)
            zoom.setContentSize(it.intrinsicSize)
        })
}

@Composable
internal expect fun PhotoViewerSystemBars(controlsVisible: Boolean)

@Composable
internal expect fun PhotoViewerOverlay(onDismissRequest: () -> Unit, content: @Composable () -> Unit)

internal object PhotoViewerPositionProvider : PopupPositionProvider {
    override fun calculatePosition(anchorBounds: IntRect, windowSize: IntSize, layoutDirection: LayoutDirection, popupContentSize: IntSize) = IntOffset.Zero
}
