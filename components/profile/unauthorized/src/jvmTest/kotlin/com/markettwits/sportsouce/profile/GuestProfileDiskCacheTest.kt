package com.markettwits.sportsouce.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.*
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.disk.DiskCache
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.markettwits.core_ui.items.image.AlbumImageSize
import com.markettwits.core_ui.items.image.albumImageUrl
import com.markettwits.sportsouce.unauthorized.domain.GuestWelcomeContent
import com.markettwits.sportsouce.unauthorized.presentation.components.GuestProfileBackground
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okio.Path.Companion.toPath
import org.jetbrains.skia.Surface
import java.nio.file.Files
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.*

@OptIn(ExperimentalComposeUiApi::class)
class GuestProfileDiskCacheTest {
    @Test fun aNewLoaderReadsThePhotoFromDiskWithoutAnyNetworkOrMemoryCache() = runBlocking {
        val folder = Files.createTempDirectory("guest-photo-cache").toFile()
        val url = "https://cdn.sportsauce.ru/live/guest-disk-test.jpg"
        val content = GuestWelcomeContent.Local.copy(imageUrl = url)
        val requests = AtomicInteger()
        val png = Surface.makeRasterN32Premul(32, 32).use { surface ->
            surface.canvas.clear(0xFF0000FF.toInt())
            surface.makeImageSnapshot().use { it.encodeToData()!!.use { data -> data.bytes } }
        }
        fun cache() = DiskCache.Builder().directory(folder.absolutePath.toPath()).maxSizeBytes(4L * 1024 * 1024).build()
        fun loader(disk: DiskCache, client: HttpClient) = ImageLoader.Builder(PlatformContext.INSTANCE)
            .diskCache(disk).components { add(KtorNetworkFetcherFactory(httpClient = { client })) }.build()
        suspend fun show(imageLoader: ImageLoader) {
            ImageComposeScene(width = 200, height = 200) {
                GuestProfileBackground(content, Modifier.fillMaxSize(), imageLoader = imageLoader)
            }.use { scene ->
                var time = System.nanoTime()
                repeat(200) {
                    delay(5); time += 40_000_000
                    val blue = scene.render(time).use { image -> image.toComposeImageBitmap().toPixelMap()[100, 100].blue }
                    if (blue > .95f) return
                }
                fail("The downloaded/cached photograph never appeared")
            }
        }
        val firstCache = cache()
        val online = HttpClient(MockEngine { requests.incrementAndGet(); respond(png, headers = headersOf(HttpHeaders.ContentType, "image/png")) })
        val first = loader(firstCache, online)
        try { show(first) }
        finally { first.shutdown(); online.close() }
        assertTrue(requests.get() > 0)
        firstCache.openSnapshot(albumImageUrl(url, AlbumImageSize.Full)).use { assertNotNull(it, "The image must be stored on disk") }
        firstCache.shutdown()
        val offlineCalls = AtomicInteger()
        val offline = HttpClient(MockEngine { offlineCalls.incrementAndGet(); error("Network is unavailable") })
        val secondCache = cache()
        val second = loader(secondCache, offline)
        try {
            show(second)
            assertEquals(0, offlineCalls.get(), "Reopening must read the persistent image rather than retry the network")
        } finally { second.shutdown(); offline.close(); secondCache.shutdown(); folder.deleteRecursively() }
    }
}
