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
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class GuestProfileRemoteImageTest {
    private val source = "https://cdn.sportsauce.ru/live/2023/August/22/05/profile.jpg"
    private val preview = albumImageUrl(source, AlbumImageSize.Preview)
    private val full = albumImageUrl(source, AlbumImageSize.Full)

    @Test fun tinyPreviewRefinesToTheResizedPhotoAndReopeningReusesTheCache() = runBlocking {
        // Original CDN URLs and links already resized by the website must use the same pipeline.
        for (url in listOf(source, albumImageUrl(source, AlbumImageSize.Cover))) {
            val photo = CompletableDeferred<Image>()
            val requests = ConcurrentLinkedQueue<String>()
            val loader = loader(requests) { target ->
                if (target == preview) ColorImage(0xFF00FF00.toInt(), 48, 48) else photo.await()
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
                            frames.until { it[100, 100].green > .95f }
                            assertEquals(setOf(preview, full), requests.toSet())
                            photo.complete(ColorImage(0xFF0000FF.toInt(), 1600, 1600))
                        }
                        frames.until {
                            val pixel = it[100, 100]
                            assertTrue(pixel.alpha > .99f, "Refinement must never expose an empty frame")
                            pixel.blue > .95f
                        }
                        assertEquals(2, requests.size, "The second visit must reuse Coil's cache")
                        assertFalse(source in requests, "The multi-megabyte original must never be downloaded")
                    }
                }
            } finally { loader.shutdown() }
        }
    }

    @Test fun failedFullImageKeepsTheBlurredPreviewWithoutDownloadingTheOriginal() = runBlocking {
        val photo = CompletableDeferred<Image>()
        val requests = ConcurrentLinkedQueue<String>()
        val loader = loader(requests) { target ->
            if (target == preview) ColorImage(0xFF00FF00.toInt(), 48, 48) else photo.await()
        }
        try {
            ImageComposeScene(width = 200, height = 200) {
                GuestProfileBackground(GuestWelcomeContent.Local.copy(imageUrl = source),
                    Modifier.fillMaxSize(), imageLoader = loader)
            }.use { scene ->
                val frames = Frames(scene)
                frames.until { it[100, 100].green > .95f }
                photo.completeExceptionally(IOException("CDN timeout"))
                repeat(30) { assertTrue(frames.next()[100, 100].green > .95f) }
                assertEquals(setOf(preview, full), requests.toSet())
                assertEquals(2, requests.size)
            }
        } finally { loader.shutdown() }
    }

    @Test fun failureOfBothSizesAlwaysRetainsTheDrawnBackground() = runBlocking {
        val requests = ConcurrentLinkedQueue<String>()
        val loader = loader(requests) { throw IOException("Offline") }
        try {
            ImageComposeScene(width = 200, height = 200) {
                GuestProfileBackground(GuestWelcomeContent.Local.copy(imageUrl = source),
                    Modifier.fillMaxSize(), imageLoader = loader)
            }.use { scene ->
                val frames = Frames(scene)
                val initial = frames.next()[100, 100]
                frames.until { requests.size == 2 }
                repeat(30) { assertEquals(initial, frames.next()[100, 100]) }
                assertEquals(setOf(preview, full), requests.toSet())
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
