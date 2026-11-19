package com.markettwits.core_ui.items.image

import androidx.compose.runtime.*
import coil3.ImageLoader
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Size
import coil3.memory.MemoryCache
import kotlinx.coroutines.delay

internal fun albumOriginalMemoryKey(url: String, size: AlbumImageSize) =
    "${albumOriginalUrl(url)}#album-${if (size == AlbumImageSize.Full) "original" else size.pixels}"

internal data class AlbumImageLoad(val state: AsyncImagePainter.State, val retry: () -> Unit)

/** Viewed photos prefer original quality; backgrounds can race equal-sized sources and keep the first success. */
@Composable
internal fun rememberAlbumImageLoad(
    url: String,
    size: AlbumImageSize,
    placeholderKey: String,
    loadOriginal: Boolean,
    imageLoader: ImageLoader,
    proxyTimeoutMillis: Long = 4_000,
    raceOriginal: Boolean = false,
): AlbumImageLoad {
    val context = LocalPlatformContext.current
    val target = remember(url, size) { albumImageUrl(url, size) }
    val original = remember(url) { albumOriginalUrl(url) }
    val hasProxy = target != original
    var fallback by remember(url, size, imageLoader) {
        mutableStateOf(hasProxy && imageLoader.memoryCache?.get(MemoryCache.Key(albumOriginalMemoryKey(url, size))) != null)
    }
    var originalLoaded by remember(url, size, imageLoader) { mutableStateOf(fallback) }
    var attempt by remember(url, size) { mutableIntStateOf(0) }
    val request = remember(context, target, size, placeholderKey) {
        ImageRequest.Builder(context).data(target).size(size.pixels)
            .memoryCacheKey(target).diskCacheKey(target).placeholderMemoryCacheKey(placeholderKey)
            .crossfade(false).build()
    }
    val proxy = if (!hasProxy || (!originalLoaded && (!fallback || loadOriginal || raceOriginal))) key(target, attempt) {
        rememberAsyncImagePainter(request, imageLoader = imageLoader)
    } else null
    val proxyState = proxy?.state?.collectAsState()?.value
    LaunchedEffect(target, proxyState, attempt) {
        if (hasProxy && !fallback && proxyState !is AsyncImagePainter.State.Success) {
            if (proxyState !is AsyncImagePainter.State.Error) delay(proxyTimeoutMillis)
            fallback = true
        }
    }
    val needsFallback = hasProxy && proxyState !is AsyncImagePainter.State.Success && (fallback || proxyState is AsyncImagePainter.State.Error)
    val direct = if (hasProxy && (loadOriginal || (raceOriginal && proxyState !is AsyncImagePainter.State.Success) || needsFallback || originalLoaded)) key(original, size, attempt) {
        val directRequest = remember(context, original, size, placeholderKey) {
            ImageRequest.Builder(context).data(original)
                // Coil keeps the original bytes on disk; bitmap decoding still has its default 4096px safety cap.
                .size(if (size == AlbumImageSize.Full) Size.ORIGINAL else Size(size.pixels, size.pixels))
                .memoryCacheKey(albumOriginalMemoryKey(url, size)).diskCacheKey(original)
                .placeholderMemoryCacheKey(placeholderKey).crossfade(false).build()
        }
        rememberAsyncImagePainter(directRequest, imageLoader = imageLoader)
    } else null
    val directState = direct?.state?.collectAsState()?.value
    SideEffect { if (directState is AsyncImagePainter.State.Success) originalLoaded = true }
    val state = when {
        directState is AsyncImagePainter.State.Success -> directState
        proxyState is AsyncImagePainter.State.Success -> proxyState
        (loadOriginal || raceOriginal) && directState is AsyncImagePainter.State.Error && proxyState !is AsyncImagePainter.State.Error ->
            proxyState ?: AsyncImagePainter.State.Empty
        needsFallback -> directState ?: AsyncImagePainter.State.Empty
        else -> proxyState ?: AsyncImagePainter.State.Empty
    }
    return AlbumImageLoad(state) {
        fallback = false
        originalLoaded = false
        attempt++
    }
}
