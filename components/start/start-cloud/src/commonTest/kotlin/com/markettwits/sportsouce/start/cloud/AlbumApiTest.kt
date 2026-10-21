package com.markettwits.sportsouce.start.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.sportsouce.start.cloud.api.start.SportSauceStartApiBase
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class AlbumApiTest {
    @Test fun albumByIdKeepsAllPhotosWithoutUsingTheStartsEndpoint() = runTest {
        val json = JsonProviderBase().provide()
        val photo = """{"id":1,"album_id":27,"file_id":10,"createdAt":"","updatedAt":"","tags":[],"file":{"id":10,"fullPath":"https://cdn.example.test/photo.jpg","name":"photo.jpg","path":"https://cdn.example.test","createdAt":"","updatedAt":"","extension":"jpg","lastModified":"1","key":"photo.jpg"}}"""
        val client = HttpClient(MockEngine { request ->
            assertEquals("/album/27", request.url.encodedPath)
            respond("""{"id":27,"start_id":309,"name":"Фото","isBeforeStart":null,"createdAt":"","updatedAt":"","start":{"id":309,"short_name":"Трейл","start_date":"2026-10-17"},"photos":[${List(448) { photo }.joinToString(",")}]}""",
                headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }) { expectSuccess = true; defaultRequest { url("https://api.sportsauce.ru/") } }
        try {
            val api = SportSauceStartApiBase(object : HttpClientProvider {
                override fun provide(loggerEnabled: Boolean) = client
                override fun json() = json
            })
            val album = api.album(27)
            assertEquals(448, album.photos.size)
            assertEquals("https://cdn.example.test/photo.jpg", album.photos.last().file.fullPath)
        } finally { client.close() }
    }
    @Test fun photosUseServerPagingAndCommaSeparatedTagsAndAcceptShortTagObjects() = runTest {
        val json = JsonProviderBase().provide()
        val requests = mutableListOf<Url>()
        val client = HttpClient(MockEngine { request ->
            requests += request.url
            respond("""{"count":229,"rows":[{"id":4944,"album_id":9,"file_id":5614,"createdAt":"","updatedAt":"","tags":[{"id":114,"name":"саша j s-iron","type":"custom"}],"file":{"id":5614,"fullPath":"https://cdn.example.test/photo.jpg","name":"photo.jpg","path":"https://cdn.example.test","createdAt":"","updatedAt":"","extension":"jpg","lastModified":"1","key":null}}]}""",
                headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }) { expectSuccess = true; defaultRequest { url("https://api.sportsauce.ru/") } }
        try {
            val api = SportSauceStartApiBase(object : HttpClientProvider {
                override fun provide(loggerEnabled: Boolean) = client
                override fun json() = json
            })
            val page = api.albumPhotos(9, 30, 30, listOf("ирина ткаченко", "саша j s-iron"))
            assertEquals(229, page.count); assertEquals("саша j s-iron", page.rows.single().tags.single().name)
            assertEquals("/album/photo", requests[0].encodedPath)
            assertEquals("9", requests[0].parameters["album_id"])
            assertEquals("30", requests[0].parameters["skipCount"])
            assertEquals("30", requests[0].parameters["maxResultCount"])
            assertEquals("ирина ткаченко,саша j s-iron", requests[0].parameters["tags"])
            api.albumPhotos(9, 0, 30, emptyList())
            assertNull(requests[1].parameters["tags"])
        } finally { client.close() }
    }

}
