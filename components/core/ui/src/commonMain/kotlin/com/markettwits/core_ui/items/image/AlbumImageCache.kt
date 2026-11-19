package com.markettwits.core_ui.items.image

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.size.Size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Warm a saved photo before mounting its network painters. Never downloads or rewrites disk entries. */
suspend fun preloadCachedAlbumImage(
    url: String,
    size: AlbumImageSize,
    imageLoader: ImageLoader,
    context: PlatformContext,
): Boolean {
    val target = albumImageUrl(url, size)
    val original = albumOriginalUrl(url)
    val candidates = buildList {
        if (target != original) add(original to albumOriginalMemoryKey(url, size))
        add(target to target)
        // Earlier welcome versions saved the 1600px proxy; reuse it locally at the new decode size.
        if (size == AlbumImageSize.Cover) {
            val previous = albumImageUrl(url, AlbumImageSize.Full)
            if (previous != target) add(previous to target)
        }
    }
    if (candidates.any { (_, key) -> imageLoader.memoryCache?.get(MemoryCache.Key(key)) != null }) return true
    return withContext(Dispatchers.Default) {
        for ((data, key) in candidates) {
            val saved = runCatching { imageLoader.diskCache?.openSnapshot(data)?.use { true } ?: false }.getOrDefault(false)
            if (!saved) continue
            val request = ImageRequest.Builder(context).data(data)
                .size(if (data == original && size == AlbumImageSize.Full) Size.ORIGINAL else Size(size.pixels, size.pixels))
                .memoryCacheKey(key).diskCacheKey(data)
                .networkCachePolicy(CachePolicy.DISABLED).diskCachePolicy(CachePolicy.READ_ONLY).build()
            if (imageLoader.execute(request) is SuccessResult) return@withContext true
        }
        false
    }
}
