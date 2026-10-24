package com.markettwits.sportsouce.starts.common

import com.markettwits.core.time.BaseTimeMapper
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

    private fun map(payload: JsonObject) = StartsCloudToListMapperBase(BaseTimeMapper())
        .mapSingle(listOf(json.decodeFromJsonElement<NetworkStartRemoteItem>(payload))).single()
}
