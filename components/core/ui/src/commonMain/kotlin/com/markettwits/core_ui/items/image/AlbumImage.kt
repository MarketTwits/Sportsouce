package com.markettwits.core_ui.items.image

import androidx.compose.foundation.Image
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
import coil3.request.crossfade
import coil3.decode.DataSource
import coil3.memory.MemoryCache
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.theme.SauceSizes
import com.markettwits.core_ui.items.theme.SauceTheme

/** Opt-in CDN loading. Failed refinements retain the preview, never fetch the original CDN image. */
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
) {
    val context = LocalPlatformContext.current
    var retained by remember(url) { mutableStateOf(initialPainter?.let { AlbumPreview(it, false) }) }
    val currentOnPainter by rememberUpdatedState(onPainter)
    val target = remember(url, size) { albumImageUrl(url, size) }
    val tiny = remember(url) { albumImageUrl(url, AlbumImageSize.Preview) }
    val cachedTarget = remember(target, imageLoader) { imageLoader.memoryCache?.get(MemoryCache.Key(target)) != null }
    val placeholderSize = remember(url, size, imageLoader) {
        // Closing onto an offscreen tile can reuse the viewed image immediately, before a grid fetch.
        val candidates = when (size) {
            AlbumImageSize.Grid -> listOf(AlbumImageSize.Full, AlbumImageSize.Preview)
            AlbumImageSize.Full -> listOf(AlbumImageSize.Grid, AlbumImageSize.Filmstrip, AlbumImageSize.Preview)
            else -> listOf(AlbumImageSize.Grid, AlbumImageSize.Preview)
        }
        candidates.firstOrNull { imageLoader.memoryCache?.get(MemoryCache.Key(albumImageUrl(url, it))) != null }
            ?: AlbumImageSize.Preview
    }
    val request = remember(context, target, size, url, placeholderSize) {
        val placeholder = albumImageUrl(url, placeholderSize)
        ImageRequest.Builder(context).data(target).size(size.pixels)
            .memoryCacheKey(target).diskCacheKey(target).placeholderMemoryCacheKey(placeholder)
            .crossfade(false).build()
    }
    val main = key(target) { rememberAsyncImagePainter(request, imageLoader = imageLoader) }
    val mainState by main.state.collectAsState()
    val success = mainState as? AsyncImagePainter.State.Success
    val cachedPreview = (mainState as? AsyncImagePainter.State.Loading)?.painter
    val reveal = remember(target) { Animatable(0f) }
    LaunchedEffect(target, success) {
        if (success != null) {
            if (retained == null || success.result.dataSource == DataSource.MEMORY_CACHE) reveal.snapTo(1f)
            else reveal.animateTo(1f, tween(180))
            retained = AlbumPreview(success.painter, false)
        }
    }
    // A cached grid image (or the tapped tile) is already a better placeholder than a new tiny fetch.
    val preview = if (success == null && retained == null && cachedPreview == null && !cachedTarget &&
        placeholderSize == AlbumImageSize.Preview && tiny != target && url.isNotBlank()) {
        val previewRequest = remember(context, tiny) {
            ImageRequest.Builder(context).data(tiny).size(AlbumImageSize.Preview.pixels)
                .memoryCacheKey(tiny).diskCacheKey(tiny).build()
        }
        val painter = rememberAsyncImagePainter(previewRequest, imageLoader = imageLoader)
        val state by painter.state.collectAsState()
        (state as? AsyncImagePainter.State.Success)?.painter
    } else null
    SideEffect {
        val loaded = when {
            retained != null -> retained
            cachedPreview != null -> AlbumPreview(cachedPreview, placeholderSize == AlbumImageSize.Preview)
            preview != null -> AlbumPreview(preview, true)
            else -> null
        }
        if (loaded != null) {
            retained = loaded
        }
        (success?.painter ?: loaded?.painter)?.let(currentOnPainter)
    }
    Box(modifier) {
        val fallback = retained ?: initialPainter?.let { AlbumPreview(it, false) }
        if (fallback != null) Image(fallback.painter, null,
            Modifier.fillMaxSize().then(if (fallback.blurred) Modifier.blur(6.dp) else Modifier), contentScale = contentScale,
            alignment = alignment)
        else if (mainState !is AsyncImagePainter.State.Error) loading()
        if (success != null) Image(success.painter, null, Modifier.fillMaxSize().graphicsLayer { alpha = reveal.value },
            contentScale = contentScale, alignment = alignment)
        if (mainState is AsyncImagePainter.State.Error) error(fallback != null) { main.restart() }
    }
}

private data class AlbumPreview(val painter: Painter, val blurred: Boolean)
