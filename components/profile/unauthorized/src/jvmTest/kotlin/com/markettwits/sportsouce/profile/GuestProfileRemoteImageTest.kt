package com.markettwits.sportsouce.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.*
import androidx.compose.ui.graphics.PixelMap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import coil3.*
import coil3.decode.DataSource
import coil3.fetch.Fetcher
import coil3.fetch.ImageFetchResult
import coil3.request.Options
import com.markettwits.core_ui.items.image.AlbumImageSize
import com.markettwits.core_ui.items.image.albumImageUrl
import com.markettwits.sportsouce.unauthorized.domain.GuestWelcomeContent
import com.markettwits.sportsouce.unauthorized.presentation.components.GuestProfileBackground
import kotlinx.coroutines.*
import java.io.IOException
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class GuestProfileRemoteImageTest {
    private val source = "https://cdn.sportsauce.ru/live/2023/August/22/05/profile.jpg"
    private val preview = albumImageUrl(source, AlbumImageSize.Preview)
    private val cover = albumImageUrl(source, AlbumImageSize.Cover)

    @Test fun tinyPreviewRefinesToTheFirstAvailablePhotoAndReopeningReusesTheCache() = runBlocking {
        for (url in listOf(source, cover)) {
            val photo = CompletableDeferred<Image>()
            val requests = ConcurrentLinkedQueue<String>()
            val cancelled = CompletableDeferred<Unit>()
            val loader = loader(requests) { target, _ ->
                when (target) {
                    preview -> ColorImage(0xFF00FF00.toInt(), 48, 48)
                    cover -> photo.await()
                    else -> try { awaitCancellation() } finally { cancelled.complete(Unit) }
                }
            }
            val content = GuestWelcomeContent.Local.copy(imageUrl = url)
            try {
                repeat(2) { visit ->
                    ImageComposeScene(width = 200, height = 200) {
                        GuestProfileBackground(content, Modifier.fillMaxSize(), imageLoader = loader)
                    }.use { scene ->
                        val frames = Frames(scene)
                        assertTrue(frames.next()[100, 100].alpha > .99f)
                        if (visit == 0) {
                            frames.until { it[100, 100].green > .95f && requests.size == 3 }
                            assertEquals(setOf(preview, cover, source), requests.toSet())
                            photo.complete(ColorImage(0xFF0000FF.toInt(), 960, 960))
                        }
                        frames.until { it[100, 100].blue > .95f }
                        assertEquals(3, requests.size, "The second visit must reuse Coil's cache")
                        assertTrue(cancelled.isCompleted, "The slow source must stop when the resized photo wins")
                    }
                }
            } finally { loader.shutdown() }
        }
    }

    @Test fun stalledProxyDoesNotDelayTheIndependentCdnAndDecodingIsBounded() = runBlocking {
        val requests = ConcurrentLinkedQueue<String>()
        val cancelled = CompletableDeferred<Unit>()
        val loader = loader(requests) { target, options ->
            if (target == source) {
                assertEquals(coil3.size.Size(960, 960), options.size)
                ColorImage(0xFF0000FF.toInt(), 960, 960)
            } else try { awaitCancellation() } finally { cancelled.complete(Unit) }
        }
        try {
            ImageComposeScene(width = 200, height = 200) {
                GuestProfileBackground(GuestWelcomeContent.Local.copy(imageUrl = source),
                    Modifier.fillMaxSize(), imageLoader = loader)
            }.use { scene ->
                // Well below the previous four-second failover delay.
                withTimeout(2000) { Frames(scene).until { it[100, 100].blue > .95f } }
                assertTrue(source in requests)
                assertTrue(cancelled.isCompleted)
                assertFalse(requests.any { "rs:fit:1600:" in it })
            }
        } finally { loader.shutdown() }
    }

    @Test fun failureOfBothSourcesRetainsThePreview() = runBlocking {
        val requests = ConcurrentLinkedQueue<String>()
        val loader = loader(requests) { target, _ ->
            if (target == preview) ColorImage(0xFF00FF00.toInt(), 48, 48) else throw IOException("Unavailable")
        }
        try {
            ImageComposeScene(width = 200, height = 200) {
                GuestProfileBackground(GuestWelcomeContent.Local.copy(imageUrl = source),
                    Modifier.fillMaxSize(), imageLoader = loader)
            }.use { scene ->
                val frames = Frames(scene)
                frames.until { it[100, 100].green > .95f }
                repeat(12) { assertTrue(frames.next()[100, 100].green > .95f) }
                assertEquals(setOf(preview, cover, source), requests.toSet())
            }
        } finally { loader.shutdown() }
    }

    @Test fun completeFailureAlwaysRetainsTheDrawnBackground() = runBlocking {
        val requests = ConcurrentLinkedQueue<String>()
        val loader = loader(requests) { _, _ -> throw IOException("Offline") }
        try {
            ImageComposeScene(width = 200, height = 200) {
                GuestProfileBackground(GuestWelcomeContent.Local.copy(imageUrl = source),
                    Modifier.fillMaxSize(), imageLoader = loader)
            }.use { scene ->
                val frames = Frames(scene)
                val initial = frames.next()[100, 100]
                frames.until { requests.size == 3 }
                repeat(12) { assertEquals(initial, frames.next()[100, 100]) }
            }
        } finally { loader.shutdown() }
    }

    private fun loader(requests: ConcurrentLinkedQueue<String>, fetch: suspend (String, Options) -> Image) =
        ImageLoader.Builder(PlatformContext.INSTANCE).components {
            add(object : Fetcher.Factory<Uri> {
                override fun create(data: Uri, options: Options, imageLoader: ImageLoader) = Fetcher {
                    requests += data.toString()
                    ImageFetchResult(fetch(data.toString(), options), false, DataSource.NETWORK)
                }
            })
        }.build()

    private class Frames(private val scene: ImageComposeScene) {
        private var time = System.nanoTime()
        suspend fun next(): PixelMap {
            delay(5)
            time += 40_000_000
            return scene.render(time).use { it.toComposeImageBitmap().toPixelMap() }
        }
        suspend fun until(condition: (PixelMap) -> Boolean) {
            repeat(200) { if (condition(next())) return }
            fail("Remote image did not reach the expected state")
        }
    }
}
