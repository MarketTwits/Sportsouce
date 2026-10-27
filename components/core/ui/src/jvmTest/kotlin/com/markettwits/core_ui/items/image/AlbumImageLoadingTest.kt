package com.markettwits.core_ui.items.image

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.PixelMap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import coil3.*
import coil3.decode.DataSource
import coil3.fetch.Fetcher
import coil3.fetch.ImageFetchResult
import coil3.request.Options
import coil3.memory.MemoryCache
import com.markettwits.core_ui.items.theme.SauceTheme
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class AlbumImageLoadingTest {
    private val source = "https://cdn.sportsauce.ru/live/photo.jpg"

    @Test fun tinyPreviewAppearsBeforeTheGridImageAndRefinesWithoutBlankFrames() = runBlocking<Unit> {
        val high = CompletableDeferred<Image>()
        val requests = ConcurrentLinkedQueue<String>()
        val loader = loader(requests) { url -> if ("rs:fit:48:" in url) red(48) else high.await() }
        var mount by mutableIntStateOf(0)
        try {
            ImageComposeScene(width = 200, height = 200) {
                SauceTheme { key(mount) { AlbumImage(source, AlbumImageSize.Grid, Modifier.fillMaxSize(), imageLoader = loader) } }
            }.use { scene ->
                val frames = Frames(scene)
                frames.until { it[100, 100].red > .95f && it[100, 100].blue < .05f }
                assertTrue(requests.contains(albumImageUrl(source, AlbumImageSize.Preview)))
                assertTrue(requests.contains(albumImageUrl(source, AlbumImageSize.Grid)))
                assertFalse(requests.contains(source))
                high.complete(blue(480))
                var sawBlend = false
                frames.until { pixels ->
                    val p = pixels[100, 100]
                    assertTrue(p.red + p.blue > .9f, "Refining the preview must never reveal a blank frame")
                    if (p.red > .1f && p.blue > .1f) sawBlend = true
                    p.blue > .95f && p.red < .05f
                }
                assertTrue(sawBlend, "The preview must crossfade into the loaded image rather than switch in one frame")
                val fetched = requests.size
                mount++
                frames.until { it[100, 100].blue > .95f }
                repeat(8) { frames.next() }
                assertEquals(fetched, requests.size, "Reopening a cached image must not fetch it again")
            }
        } finally { loader.shutdown() }
    }

    @Test fun fullscreenLoadsProxyAndOriginalTogetherThenFadesIntoOriginalQuality() = runBlocking<Unit> {
        val full = CompletableDeferred<Image>()
        val original = CompletableDeferred<Image>()
        val requests = ConcurrentLinkedQueue<String>()
        val loader = loader(requests) { url ->
            when {
                url == source -> original.await()
                "rs:fit:1600:" in url -> full.await()
                else -> red(480)
            }
        }
        try {
            ImageComposeScene(width = 200, height = 200) {
                SauceTheme { AlbumImage(source, AlbumImageSize.Full, Modifier.fillMaxSize(), imageLoader = loader, loadOriginal = true) }
            }.use { scene ->
                val frames = Frames(scene)
                frames.until { requests.contains(source) && requests.contains(albumImageUrl(source, AlbumImageSize.Full)) }
                frames.until { it[100, 100].red > .95f }
                full.complete(blue(1600))
                frames.until { it[100, 100].blue > .95f }
                original.complete(green(4000))
                var sawBlend = false
                frames.until {
                    val p = it[100, 100]
                    assertTrue(p.blue + p.green > .9f, "The original must replace the optimized image without a blank frame")
                    if (p.blue > .1f && p.green > .1f) sawBlend = true
                    p.green > .95f
                }
                assertTrue(sawBlend)
                assertNotNull(loader.memoryCache!![MemoryCache.Key(albumOriginalMemoryKey(source, AlbumImageSize.Full))])
            }
        } finally { loader.shutdown() }
    }

    @Test fun gridBypassesFailedProxyAndKeepsThePreviewUntilOriginalArrives() = runBlocking<Unit> {
        val original = CompletableDeferred<Image>()
        val requests = ConcurrentLinkedQueue<String>()
        val loader = loader(requests) { url ->
            when {
                url == source -> original.await()
                "rs:fit:48:" in url -> red(48)
                else -> throw IOException("Proxy cannot resize this image")
            }
        }
        var errorShown = false
        try {
            ImageComposeScene(width = 200, height = 200) {
                SauceTheme { AlbumImage(source, AlbumImageSize.Grid, Modifier.fillMaxSize(), imageLoader = loader,
                    error = { _, _ -> errorShown = true }) }
            }.use { scene ->
                val frames = Frames(scene)
                frames.until { it[100, 100].red > .95f && requests.contains(source) }
                assertFalse(errorShown, "Proxy failure is recoverable while the original is loading")
                original.complete(blue(480))
                frames.until { it[100, 100].blue > .95f }
                assertFalse(errorShown)
                assertEquals(1, requests.count { it == source })
            }
        } finally { loader.shutdown() }
    }

    @Test fun bothSourcesFailRetainingPreviewAndRetryCanRecover() = runBlocking<Unit> {
        val requests = ConcurrentLinkedQueue<String>()
        var recover = false
        val loader = loader(requests) { url ->
            when {
                recover -> blue(1600)
                "rs:fit:48:" in url -> red(48)
                else -> throw IOException("Unavailable")
            }
        }
        var retry: (() -> Unit)? = null
        var previewOnError = false
        try {
            ImageComposeScene(width = 200, height = 200) {
                SauceTheme { AlbumImage(source, AlbumImageSize.Full, Modifier.fillMaxSize(), imageLoader = loader,
                    error = { hasPreview, action -> previewOnError = hasPreview; retry = action }) }
            }.use { scene ->
                val frames = Frames(scene)
                frames.until { retry != null && it[100, 100].red > .95f }
                assertTrue(previewOnError)
                assertTrue(requests.contains(source))
                recover = true
                checkNotNull(retry).invoke()
                frames.until { it[100, 100].blue > .95f }
            }
        } finally { loader.shutdown() }
    }

    @Test fun originalFailureDoesNotHideASuccessfulProxyImage() = runBlocking<Unit> {
        val requests = ConcurrentLinkedQueue<String>()
        val loader = loader(requests) { url -> if (url == source) throw IOException("CDN unavailable") else blue(1600) }
        var errorShown = false
        try {
            ImageComposeScene(width = 200, height = 200) {
                SauceTheme { AlbumImage(source, AlbumImageSize.Full, Modifier.fillMaxSize(), imageLoader = loader,
                    loadOriginal = true, error = { _, _ -> errorShown = true }) }
            }.use { scene ->
                val frames = Frames(scene)
                frames.until { it[100, 100].blue > .95f && requests.contains(source) }
                repeat(10) { frames.next() }
                assertFalse(errorShown)
            }
        } finally { loader.shutdown() }
    }

    @Test fun stalledProxyIsCancelledAndOriginalUsesIndependentCacheKeys() = runBlocking<Unit> {
        val requests = ConcurrentLinkedQueue<String>()
        val cancelled = CompletableDeferred<Unit>()
        val loader = loader(requests) { url ->
            if (url == source) blue(480) else {
                try { kotlinx.coroutines.awaitCancellation() }
                finally { cancelled.complete(Unit) }
            }
        }
        var state: coil3.compose.AsyncImagePainter.State = coil3.compose.AsyncImagePainter.State.Empty
        try {
            ImageComposeScene(width = 200, height = 200) {
                state = rememberAlbumImageLoad(source, AlbumImageSize.Grid,
                    albumImageUrl(source, AlbumImageSize.Preview), false, loader, proxyTimeoutMillis = 120).state
            }.use { scene ->
                val frames = Frames(scene)
                frames.until { state is coil3.compose.AsyncImagePainter.State.Success }
                assertTrue(cancelled.isCompleted, "The abandoned proxy request must be cancelled")
                assertTrue(requests.contains(source))
                assertNull(loader.memoryCache!![MemoryCache.Key(albumImageUrl(source, AlbumImageSize.Grid))])
                assertNotNull(loader.memoryCache!![MemoryCache.Key(albumOriginalMemoryKey(source, AlbumImageSize.Grid))])
            }
        } finally { loader.shutdown() }
    }

    @Test fun fullQualityFetchIsCancelledWhenThePhotoStopsBeingActive() = runBlocking<Unit> {
        val requests = ConcurrentLinkedQueue<String>()
        val cancelled = CompletableDeferred<Unit>()
        val loader = loader(requests) { url ->
            if (url == source) {
                try { kotlinx.coroutines.awaitCancellation() }
                finally { cancelled.complete(Unit) }
            } else blue(1600)
        }
        var active by mutableStateOf(true)
        try {
            ImageComposeScene(width = 200, height = 200) {
                SauceTheme { AlbumImage(source, AlbumImageSize.Full, Modifier.fillMaxSize(), imageLoader = loader, loadOriginal = active) }
            }.use { scene ->
                val frames = Frames(scene)
                frames.until { requests.contains(source) && it[100, 100].blue > .95f }
                active = false
                frames.until { cancelled.isCompleted }
                assertTrue(frames.next()[100, 100].blue > .95f)
            }
        } finally { loader.shutdown() }
    }

    @Test fun cachedOriginalRemountsWithoutProxyAndLateProxyCannotDowngradeIt() = runBlocking<Unit> {
        val proxy = CompletableDeferred<Image>()
        val requests = ConcurrentLinkedQueue<String>()
        val loader = loader(requests) { url -> if (url == source) green(4000) else proxy.await() }
        var mount by mutableIntStateOf(0)
        try {
            ImageComposeScene(width = 200, height = 200) {
                SauceTheme { key(mount) {
                    AlbumImage(source, AlbumImageSize.Full, Modifier.fillMaxSize(), imageLoader = loader, loadOriginal = true)
                } }
            }.use { scene ->
                val frames = Frames(scene)
                frames.until { it[100, 100].green > .95f && requests.contains(albumImageUrl(source, AlbumImageSize.Full)) }
                proxy.complete(blue(1600))
                repeat(12) { assertTrue(frames.next()[100, 100].green > .95f, "A late proxy response must never replace the original") }
                val fetched = requests.size
                mount++
                frames.until { it[100, 100].green > .95f }
                repeat(8) { frames.next() }
                assertEquals(fetched, requests.size, "The cached original must be reused without another network request")
            }
        } finally { loader.shutdown() }
    }

    @Test fun returningToAnUnloadedGridTileReusesTheViewedImageImmediately() = runBlocking<Unit> {
        val pendingGrid = CompletableDeferred<Image>()
        val requests = ConcurrentLinkedQueue<String>()
        val loader = loader(requests) { pendingGrid.await() }
        loader.memoryCache!![MemoryCache.Key(albumImageUrl(source, AlbumImageSize.Full))] = MemoryCache.Value(blue(1600))
        try {
            ImageComposeScene(width = 200, height = 200) {
                SauceTheme { AlbumImage(source, AlbumImageSize.Grid, Modifier.fillMaxSize(), imageLoader = loader) }
            }.use { scene ->
                val frames = Frames(scene)
                frames.until { it[100, 100].blue > .95f }
                assertFalse(requests.contains(albumImageUrl(source, AlbumImageSize.Preview)))
                assertFalse(requests.contains(source))
            }
        } finally { loader.shutdown() }
    }

    @Test fun firstNetworkImageFadesInButMemoryCacheRemountDoesNotReplayTheFade() = runBlocking<Unit> {
        val pending = CompletableDeferred<Image>()
        val requests = ConcurrentLinkedQueue<String>()
        val loader = loader(requests) { pending.await() }
        var mount by mutableIntStateOf(0)
        try {
            ImageComposeScene(width = 200, height = 200) {
                SauceTheme {
                    Box(Modifier.fillMaxSize().background(Color.Black)) {
                        key(mount) { AlbumImage(source, AlbumImageSize.Preview, Modifier.fillMaxSize(), imageLoader = loader, loading = {}) }
                    }
                }
            }.use { scene ->
                val frames = Frames(scene)
                frames.until { requests.isNotEmpty() }
                assertEquals(0f, frames.next()[100, 100].blue)
                pending.complete(blue(48))
                var sawFade = false
                frames.until {
                    val b = it[100, 100].blue
                    if (b in .05f.. .9f) sawFade = true
                    b > .99f
                }
                assertTrue(sawFade, "A first network image must also fade in, without requiring an existing preview")
                mount++
                frames.until { it[100, 100].blue > .99f }
                repeat(10) { assertTrue(frames.next()[100, 100].blue > .99f, "Cached remount must remain visible") }
                assertEquals(1, requests.size)
            }
        } finally { loader.shutdown() }
    }

    private fun loader(requests: ConcurrentLinkedQueue<String>, fetch: suspend (String) -> Image) =
        ImageLoader.Builder(PlatformContext.INSTANCE).components {
            add(object : Fetcher.Factory<Uri> {
                override fun create(data: Uri, options: Options, imageLoader: ImageLoader) = Fetcher {
                    requests += data.toString()
                    ImageFetchResult(fetch(data.toString()), false, DataSource.NETWORK)
                }
            })
        }.build()

    private fun red(size: Int) = ColorImage(0xFFFF0000.toInt(), size, size)
    private fun green(size: Int) = ColorImage(0xFF00FF00.toInt(), size, size)
    private fun blue(size: Int) = ColorImage(0xFF0000FF.toInt(), size, size)

    private class Frames(val scene: ImageComposeScene) {
        private var time = System.nanoTime()
        suspend fun next(): PixelMap {
            delay(5)
            time += 40_000_000
            return scene.render(time).use { it.toComposeImageBitmap().toPixelMap() }
        }
        suspend fun until(condition: (PixelMap) -> Boolean) {
            repeat(200) { if (condition(next())) return }
            fail("The image pipeline did not reach the expected state")
        }
    }
}
