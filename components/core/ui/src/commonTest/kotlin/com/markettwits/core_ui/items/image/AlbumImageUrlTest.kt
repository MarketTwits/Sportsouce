package com.markettwits.core_ui.items.image

import kotlin.test.*

class AlbumImageUrlTest {
    private val source = "https://cdn.sportsauce.ru/live/2023/August/22/05/photo.jpg"

    @Test fun variantsAreBoundedAndHaveDistinctStableCacheUrls() {
        val urls = AlbumImageSize.entries.map { albumImageUrl(source, it) }
        assertEquals(AlbumImageSize.entries.size, urls.toSet().size)
        assertEquals("https://img.sportsauce.ru/insecure/rs:fit:1600:1600/q:82/plain/$source", albumImageUrl(source, AlbumImageSize.Full))
        assertTrue(albumImageUrl(source, AlbumImageSize.Preview).contains("rs:fit:48:48/q:40"))
        assertTrue(albumImageUrl(source, AlbumImageSize.Grid).contains("rs:fit:480:480/q:72"))
        assertTrue(AlbumImageSize.entries.all { it.pixels <= 1600 })
    }

    @Test fun resizingExistingPlainProxyUrlsDoesNotNestTheProxy() {
        val web = "https://img.sportsauce.ru/insecure/rs:fit:1600:0/plain/$source"
        assertEquals(albumImageUrl(source, AlbumImageSize.Grid), albumImageUrl(web, AlbumImageSize.Grid))
        val query = "$source?version=a%20b&name=one@two"
        assertEquals(albumImageUrl(query, AlbumImageSize.Full), albumImageUrl(albumImageUrl(query, AlbumImageSize.Cover), AlbumImageSize.Full))
    }

    @Test fun plainUrlsEscapeSpecialCharactersAndRetainQueryParameters() {
        val url = albumImageUrl("$source?name=a%20b&user=one@two", AlbumImageSize.Grid)
        assertTrue(url.endsWith("photo.jpg%3Fname%3Da%2520b%26user%3Done%40two"), url)
        assertFalse(url.contains('?'))
        assertFalse(url.contains('@'))
    }

    @Test fun unrelatedAndLocalImagesDoNotGoThroughTheSportsauceProxy() {
        for (url in listOf("", "file:///tmp/photo.jpg", "data:image/png;base64,abc", "https://example.org/p.jpg", "https://cdn.sportsauce.ru.other.org/p.jpg")) {
            assertEquals(url, albumImageUrl(url, AlbumImageSize.Grid))
        }
    }
}
