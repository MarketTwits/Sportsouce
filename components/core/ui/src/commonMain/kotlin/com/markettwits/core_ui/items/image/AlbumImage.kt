package com.markettwits.core_ui.items.image

import androidx.compose.foundation.Image
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.decode.DataSource
import coil3.memory.MemoryCache
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.theme.SauceSizes
import com.markettwits.core_ui.items.theme.SauceTheme

/** Progressive album loading with an independent original-CDN fallback. */
@Composable
fun AlbumImage(
    url: String,
    size: AlbumImageSize,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    initialPainter: Painter? = null,
    onPainter: (Painter) -> Unit = {},
    loading: @Composable BoxScope.() -> Unit = {
        Box(Modifier.matchParentSize().shimmer().background(SauceTheme.colors.surfaceSubtle))
    },
    error: @Composable BoxScope.(hasPreview: Boolean, retry: () -> Unit) -> Unit = { hasPreview, _ ->
        if (!hasPreview) Icon(Icons.Outlined.ImageNotSupported, "Фото недоступно",
            Modifier.align(Alignment.Center).size(SauceSizes.icon), tint = SauceTheme.colors.textMuted)
    },
    imageLoader: ImageLoader = SingletonImageLoader.get(LocalPlatformContext.current),
    alignment: Alignment = Alignment.Center,
    loadOriginal: Boolean = false,
) {
    val context = LocalPlatformContext.current
    var retained by remember(url) { mutableStateOf(initialPainter?.let { AlbumPreview(it, false) }) }
    var lastSuccessPainter by remember(url) { mutableStateOf<Painter?>(null) }
    val currentOnPainter by rememberUpdatedState(onPainter)
    val target = remember(url, size) { albumImageUrl(url, size) }
    val tiny = remember(url) { albumImageUrl(url, AlbumImageSize.Preview) }
    val cachedTarget = remember(target, imageLoader) { imageLoader.memoryCache?.get(MemoryCache.Key(target)) != null }
    val placeholderKey = remember(url, size, imageLoader) {
        // Closing onto an offscreen tile can reuse the viewed image immediately, before a grid fetch.
        val candidates = when (size) {
            AlbumImageSize.Grid -> listOf(AlbumImageSize.Full, AlbumImageSize.Preview)
            AlbumImageSize.Full -> listOf(AlbumImageSize.Grid, AlbumImageSize.Filmstrip, AlbumImageSize.Preview)
            else -> listOf(AlbumImageSize.Grid, AlbumImageSize.Preview)
        }
        (listOf(albumOriginalMemoryKey(url, AlbumImageSize.Full), albumOriginalMemoryKey(url, size)) +
            candidates.flatMap { listOf(albumOriginalMemoryKey(url, it), albumImageUrl(url, it)) })
            .firstOrNull { imageLoader.memoryCache?.get(MemoryCache.Key(it)) != null } ?: tiny
    }
    val main = rememberAlbumImageLoad(url, size, placeholderKey, loadOriginal, imageLoader)
    val mainState = main.state
    val success = mainState as? AsyncImagePainter.State.Success
    // The original can arrive before the proxy's fade finishes. Fade from that image, never back to the tiny preview.
    val transitionPreview = remember(target, success?.painter) { lastSuccessPainter?.let { AlbumPreview(it, false) } }
    val cachedPreview = (mainState as? AsyncImagePainter.State.Loading)?.painter
    val reveal = remember(target, success?.painter) { Animatable(0f) }
    val previewReveal = remember(url) { Animatable(if (initialPainter != null) 1f else 0f) }
    LaunchedEffect(target, success) {
        if (success != null) {
            if (success.result.dataSource == DataSource.MEMORY_CACHE) reveal.snapTo(1f)
            else reveal.animateTo(1f, tween(320, easing = FastOutSlowInEasing))
            // Keep the previous preview behind the new image for the entire fade.
            retained = AlbumPreview(success.painter, false)
        }
    }
    // A cached grid image (or the tapped tile) is already a better placeholder than a new tiny fetch.
    val preview = if (success == null && retained == null && cachedPreview == null && !cachedTarget &&
        placeholderKey == tiny && tiny != target && url.isNotBlank()) {
        val previewRequest = remember(context, tiny) {
            ImageRequest.Builder(context).data(tiny).size(AlbumImageSize.Preview.pixels)
                .memoryCacheKey(tiny).diskCacheKey(tiny).build()
        }
        val painter = rememberAsyncImagePainter(previewRequest, imageLoader = imageLoader)
        val state by painter.state.collectAsState()
        (state as? AsyncImagePainter.State.Success)?.let {
            AlbumPreview(it.painter, true, it.result.dataSource == DataSource.MEMORY_CACHE)
        }
    } else null
    SideEffect {
        if (success != null) lastSuccessPainter = success.painter
        val loaded = when {
            retained != null -> retained
            cachedPreview != null -> AlbumPreview(cachedPreview, placeholderKey == tiny)
            preview != null -> preview
            else -> null
        }
        if (loaded != null) {
            retained = loaded
        }
        (success?.painter ?: loaded?.painter)?.let(currentOnPainter)
    }
    val fallback = transitionPreview ?: retained ?: initialPainter?.let { AlbumPreview(it, false) }
    LaunchedEffect(url, fallback) {
        if (fallback != null) {
            if (fallback.immediate) previewReveal.snapTo(1f)
            else previewReveal.animateTo(1f, tween(220, easing = FastOutSlowInEasing))
        }
    }
    Box(modifier) {
        if (mainState !is AsyncImagePainter.State.Error &&
            (fallback == null || previewReveal.value < 1f) && (success == null || reveal.value < 1f)) loading()
        if (fallback != null && (success == null || reveal.value < 1f)) Image(fallback.painter, null,
            Modifier.fillMaxSize().graphicsLayer { alpha = previewReveal.value }
                .then(if (fallback.blurred) Modifier.blur(6.dp) else Modifier), contentScale = contentScale,
            alignment = alignment)
        if (success != null) Image(success.painter, null, Modifier.fillMaxSize().graphicsLayer { alpha = reveal.value },
            contentScale = contentScale, alignment = alignment)
        if (mainState is AsyncImagePainter.State.Error) error(fallback != null, main.retry)
    }
}

private data class AlbumPreview(val painter: Painter, val blurred: Boolean, val immediate: Boolean = true)
