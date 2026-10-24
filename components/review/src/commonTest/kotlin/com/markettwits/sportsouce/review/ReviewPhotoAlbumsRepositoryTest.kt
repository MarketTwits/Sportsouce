package com.markettwits.sportsouce.review

import com.markettwits.cahce.Cache
import com.markettwits.sportsouce.review.review.data.ReviewPhotoAlbumsRepositoryBase
import com.markettwits.sportsouce.review.review.domain.ReviewPhotoAlbum
import com.markettwits.sportsouce.starts.albums.domain.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class ReviewPhotoAlbumsRepositoryTest {
    @Test fun skipsEmptyAlbumsKeepsStartContextAndDeduplicatesAlbumIds() = runTest {
        val cache = MemoryCache()
        val source = object : StartsAlbumsRepository {
            override suspend fun albums(offset: Int, limit: Int, filterText: String, year: Int?): StartAlbumsPage {
                assertEquals(0, offset); assertEquals(12, limit); assertEquals("", filterText); assertNull(year)
                val albums = (1..15).map { StartAlbumsItem.Album(it, "Фото $it", if (it == 1) 0 else 100, "https://cdn.example/$it.jpg") }
                return StartAlbumsPage(listOf(StartAlbumsItem(309, "Юрманка трейл", "17 октября 2026", albums),
                    StartAlbumsItem(310, "Другой старт", "18 октября 2026", listOf(albums[2]))), 2)
            }
        }
        val items = ReviewPhotoAlbumsRepositoryBase(source, cache).albums().single()
        assertEquals((2..11).toList(), items.map { it.id })
        assertTrue(items.all { it.startName == "Юрманка трейл" && it.date == "17 октября 2026" && it.photosCount == 100 })
        assertEquals("https://cdn.example/2.jpg", items.first().previewUrl)
        assertEquals(items, cache.value)
    }

    @Test fun emitsCachedAlbumsBeforeRefreshAndKeepsCacheWhenOffline() = runTest {
        val old = listOf(ReviewPhotoAlbum(27, "Юрманка трейл", "Фото", "17 октября 2026", 448, "https://cdn.example/cover.jpg"))
        val cache = MemoryCache().apply { value = old }
        var fail = true
        val source = object : StartsAlbumsRepository {
            override suspend fun albums(offset: Int, limit: Int, filterText: String, year: Int?): StartAlbumsPage {
                if (fail) error("offline")
                return StartAlbumsPage(emptyList(), 0)
            }
        }
        val emitted = mutableListOf<List<ReviewPhotoAlbum>>()
        val repo = ReviewPhotoAlbumsRepositoryBase(source, cache)
        assertFailsWith<IllegalStateException> { repo.albums().collect { emitted += it } }
        assertEquals(listOf(old), emitted); assertEquals(old, cache.value)
        fail = false
        assertEquals(listOf(old, emptyList()), repo.albums().toList())
        assertEquals(emptyList(), cache.value, "A successful empty response should clear obsolete albums")
    }

    private class MemoryCache : Cache<List<ReviewPhotoAlbum>> {
        var value: List<ReviewPhotoAlbum>? = null
        override suspend fun get(key: Any) = value
        override suspend fun set(key: Any, value: List<ReviewPhotoAlbum>) { this.value = value }
        override suspend fun getList(key: Any) = emptyList<List<ReviewPhotoAlbum>>()
        override suspend fun clear() { value = null }
    }
}
