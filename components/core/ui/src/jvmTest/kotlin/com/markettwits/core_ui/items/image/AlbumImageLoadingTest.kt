package com.markettwits.core_ui.items.image

import androidx.compose.foundation.layout.fillMaxSize
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

    @Test fun tinyPreviewAppearsBeforeTheGridImageAndRefinesWithoutBlankFrames() = runBlocking {
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
                frames.until { pixels ->
                    val p = pixels[100, 100]
                    assertTrue(p.red + p.blue > .9f, "Refining the preview must never reveal a blank frame")
                    p.blue > .95f && p.red < .05f
                }
                val fetched = requests.size
                mount++
                frames.until { it[100, 100].blue > .95f }
                repeat(8) { frames.next() }
                assertEquals(fetched, requests.size, "Reopening a cached image must not fetch it again")
            }
        } finally { loader.shutdown() }
    }

    @Test fun upgradingToFullscreenRetainsGridPreviewOnFailureAndRetryUsesOnlyTheProxy() = runBlocking {
        val full = CompletableDeferred<Image>()
        val retried = CompletableDeferred<Image>()
        val attempts = AtomicInteger()
        val requests = ConcurrentLinkedQueue<String>()
        val loader = loader(requests) { url ->
            when {
                "rs:fit:48:" in url -> red(48)
                "rs:fit:480:" in url -> red(480)
                attempts.incrementAndGet() == 1 -> full.await()
                else -> retried.await()
            }
        }
        var size by mutableStateOf(AlbumImageSize.Grid)
        var retry: (() -> Unit)? = null
        var previewOnError = false
        try {
            ImageComposeScene(width = 200, height = 200) {
                SauceTheme {
                    AlbumImage(source, size, Modifier.fillMaxSize(), imageLoader = loader,
                        error = { hasPreview, action -> previewOnError = hasPreview; retry = action })
                }
            }.use { scene ->
                val frames = Frames(scene)
                frames.until { it[100, 100].red > .95f && requests.contains(albumImageUrl(source, AlbumImageSize.Grid)) }
                repeat(8) { frames.next() }
                size = AlbumImageSize.Full
                frames.until { requests.contains(albumImageUrl(source, AlbumImageSize.Full)) }
                assertTrue(frames.next()[100, 100].red > .95f)
                full.completeExceptionally(IOException("Simulated CDN timeout"))
                frames.until { retry != null }
                assertTrue(previewOnError)
                assertTrue(frames.next()[100, 100].red > .95f, "A failed refinement must retain the preview")
                checkNotNull(retry).invoke()
                frames.until { attempts.get() == 2 }
                retried.complete(blue(1600))
                frames.until { it[100, 100].blue > .95f }
                assertTrue(requests.all { it.startsWith("https://img.sportsauce.ru/") })
                assertEquals(2, attempts.get())
            }
        } finally { loader.shutdown() }
    }

    @Test fun returningToAnUnloadedGridTileReusesTheViewedImageImmediately() = runBlocking {
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
