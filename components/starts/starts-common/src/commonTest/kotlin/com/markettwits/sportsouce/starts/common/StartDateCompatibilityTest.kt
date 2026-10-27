package com.markettwits.sportsouce.starts.common

import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.sportsouce.starts.cloud.SportSauceNetworkStartsApi
import com.markettwits.sportsouce.starts.common.data.SportSauceStartsApiBase
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import com.markettwits.sportsouce.starts.cloud.model.NetworkStartRemoteItem
import com.markettwits.sportsouce.starts.common.data.mapper.StartsCloudToListMapperBase
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import kotlin.test.*

class StartDateCompatibilityTest {
    private val json = Json { ignoreUnknownKeys = true }
    private val payload = """{
        "afishaLinkFile":null,"afisha_link":null,"archive_start_code":null,"city":null,"city_id":null,
        "close_reg":null,"conditionFile":null,"condition_file":null,"condition_short":null,
        "coordinates":null,"copernico_id":null,"copernico_preset":null,"createdAt":"2026-10-17T00:00:00Z",
        "description":null,"driving_directions":null,"end_date":"2026-10-18T10:00:00Z","finish_reg":null,
        "id":309,"isClosed":null,"isOpen":"false","isRescheduled":null,"kindOfSports":[],"name":"Юрманка трейл",
        "on_main_page":false,"payment_disabled":null,"payment_type":null,"photos_link":null,"posterLinkFile":null,
        "poster_link":null,"rang":null,"reg_link":null,"reg_on_loc":null,"reg_on_site":null,
        "registration_end_date":"2026-10-16T00:00:00Z","registration_start_date":"2026-01-01T00:00:00Z",
        "result_columns_json":null,"result_json":null,"results":[],"results_link":null,"search_service":null,
        "season_id":null,"serial_number":null,"short_name":null,"slug":"yurmanka-trejl-309",
        "start_date":"2026-10-17T00:00:00Z","start_reg":null,"start_status":{"code":6,"name":"Старт завершён"},
        "start_time":null,"status":null,"updatedAt":"2026-10-17T00:00:00Z","viewsCount":1
    }"""

    @Test fun listDisplaysRangeButKeepsOriginalDateAndNavigationIdentity() {
        val start = map(json.parseToJsonElement(payload).jsonObject)
        assertEquals("17–18 октября 2026", start.displayDate)
        assertEquals("17 октября 2026", start.date)
        assertEquals(309, start.id)
        assertEquals("yurmanka-trejl-309", start.slug)
        assertEquals(start, json.decodeFromString<StartsListItem>(json.encodeToString(start)))
    }

    @Test fun optionalEndDateAndOldCachedCardsRemainReadable() {
        val fields = json.parseToJsonElement(payload).jsonObject
        for (end in listOf(null, JsonNull, JsonPrimitive(""), JsonPrimitive("2026-10-17"), JsonPrimitive("invalid"))) {
            val start = map(JsonObject(fields - "end_date" + if (end == null) emptyMap() else mapOf("end_date" to end)))
            assertEquals("17 октября 2026", start.displayDate)
            assertNull(start.dateRange)
        }
        val oldCache = JsonObject(json.encodeToJsonElement(map(fields)).jsonObject - "dateRange")
        val restored = json.decodeFromJsonElement<StartsListItem>(oldCache)
        assertEquals("17 октября 2026", restored.displayDate)
        assertEquals(309, restored.id)
    }

    @Test fun archiveSortsByOriginalDateAcrossYearsAndRemovesDuplicateIds() = runTest {
        val base = json.parseToJsonElement(payload).jsonObject
        fun row(id: Int, date: String) = JsonObject(base + mapOf("id" to JsonPrimitive(id), "start_date" to JsonPrimitive(date), "end_date" to JsonNull))
        val response = buildJsonObject {
            put("count", 4)
            put("rows", JsonArray(listOf(row(3, "2026-01-02T00:00:00Z"), row(2, "2025-12-30T00:00:00Z"),
                row(1, "2025-01-10T00:00:00Z"), row(2, "2025-12-30T00:00:00Z"))))
        }
        val client = HttpClient(MockEngine { request ->
            assertEquals("/start/1/related-start-archive", request.url.encodedPath)
            respond(response.toString(), headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }) { expectSuccess = true; defaultRequest { url("https://api.example.test/") } }
        try {
            val api = SportSauceStartsApiBase(SportSauceNetworkStartsApi(object : HttpClientProvider {
                override fun provide(loggerEnabled: Boolean) = client
                override fun json() = json
            }), StartsCloudToListMapperBase(BaseTimeMapper()))
            val starts = api.fetchArchiveStarts(1)
            assertEquals(listOf(1, 2, 3), starts.map { it.id })
            assertEquals(listOf("10 января 2025", "30 декабря 2025", "2 января 2026"), starts.map { it.displayDate })
        } finally { client.close() }
    }

    @Test fun availabilityUsesPublishedAlbumsAndResultsIndependently() {
        val fields = json.parseToJsonElement(payload).jsonObject
        val protocol = buildJsonObject {
            put("id", 1); put("start_id", 309); put("name", "Протокол")
            put("createdAt", "2026-10-17T00:00:00Z"); put("updatedAt", "2026-10-17T00:00:00Z")
        }
        for (albums in listOf(null, JsonNull, JsonPrimitive(false), JsonPrimitive(true))) {
            for (results in listOf(JsonArray(emptyList()), JsonArray(listOf(protocol)))) {
                for (link in listOf(JsonNull, JsonPrimitive(""), JsonPrimitive("  "), JsonPrimitive("https://example.test/results"))) {
                    val start = map(JsonObject(fields + mapOf("results" to results, "results_link" to link) +
                        if (albums == null) emptyMap() else mapOf("is_has_albums" to albums)))
                    assertEquals(albums == JsonPrimitive(true), start.hasAlbums)
                    assertEquals(results.isNotEmpty() || link == JsonPrimitive("https://example.test/results"), start.hasResults)
                    assertEquals(start, json.decodeFromString<StartsListItem>(json.encodeToString(start)))
                }
            }
        }
    }

    @Test fun cachedCardsWithoutAvailabilityRemainReadable() {
        val current = map(json.parseToJsonElement(payload).jsonObject).copy(hasAlbums = true, hasResults = true)
        val cached = JsonObject(json.encodeToJsonElement(current).jsonObject - "hasAlbums" - "hasResults")
        val restored = json.decodeFromJsonElement<StartsListItem>(cached)
        assertFalse(restored.hasAlbums)
        assertFalse(restored.hasResults)
        assertEquals(current.id, restored.id)
        assertEquals(current.slug, restored.slug)
    }

    private fun map(payload: JsonObject) = StartsCloudToListMapperBase(BaseTimeMapper())
        .mapSingle(listOf(json.decodeFromJsonElement<NetworkStartRemoteItem>(payload))).single()
}
