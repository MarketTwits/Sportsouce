package com.markettwits.sportsouce.starts.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.JsonProviderBase
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class PhotoArchiveApiTest {
    @Test fun archiveUsesOffsetLimitAndOptionalFiltersAndAcceptsMissingPreview() = runTest {
        val json = JsonProviderBase().provide()
        var calls = 0
        val client = HttpClient(MockEngine { request ->
            assertEquals("/photo-archive/starts", request.url.encodedPath)
            assertEquals("10", request.url.parameters["maxResultCount"])
            if (calls++ == 0) {
                assertEquals("0", request.url.parameters["skipCount"])
                assertNull(request.url.parameters["filterText"]); assertNull(request.url.parameters["year"])
            } else {
                assertEquals("10", request.url.parameters["skipCount"])
                assertEquals("Страшно", request.url.parameters["filterText"])
                assertEquals("2025", request.url.parameters["year"])
            }
            respond("""{"rows":[{"id":258,"name":"Страшно добрый забег","short_name":null,"start_date":"2025-10-26T10:00:00.000Z","albums_count":2,"photos_count":5,"albums":[{"id":15,"name":"Пусто","isBeforeStart":null,"photos_count":0},{"id":16,"name":"Фото","photos_count":5,"preview_photo_path":"https://cdn.example.test/cover.jpg"}]}],"count":13}""",
                headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }) { expectSuccess = true; defaultRequest { url("https://api.sportsauce.ru/") } }
        try {
            val api = SportSauceNetworkStartsApi(object : HttpClientProvider {
                override fun provide(loggerEnabled: Boolean) = client
                override fun json() = json
            })
            val page = api.photoArchive(0, 10, "", null)
            assertEquals(13, page.count); assertNull(page.rows.single().albums.first().previewPhotoPath)
            assertEquals(5, page.rows.single().albums.last().photosCount)
            api.photoArchive(10, 10, "  Страшно  ", 2025)
            assertEquals(2, calls)
        } finally { client.close() }
    }
}
