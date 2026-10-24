package com.markettwits.sportsouce.start

import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.sportsouce.start.cloud.model.start.*
import com.markettwits.sportsouce.start.data.start.mapper.start.StartRemoteToUiMapperBase
import com.markettwits.sportsouce.start.data.start.mapper.albums.StartAlbumsToUiMapperBase
import com.markettwits.sportsouce.start.data.start.mapper.comments.StartCommentsToUiMapperBase
import com.markettwits.sportsouce.start.data.start.mapper.members.StartMembersToUiMapperBase
import com.markettwits.sportsouce.start.data.start.mapper.result.StartMembersResultsToUiMapperBase
import com.markettwits.sportsouce.start.data.start.mapper.review.StartReviewToUiMapperBase
import com.markettwits.sportsouce.start.data.start.mapper.time.StartTimesMapperBase
import com.markettwits.sportsouce.start.domain.mapper.StartItemToStartsListItemMapper
import com.markettwits.sportsouce.start.domain.mapper.StartsListItemToStartItemMapper
import kotlinx.serialization.json.*
import kotlin.test.*

class StartDateMappingTest {
    private val json = Json { ignoreUnknownKeys = true }
    private val time = BaseTimeMapper()
    private val mapper = StartRemoteToUiMapperBase(time, StartMembersToUiMapperBase(),
        StartMembersResultsToUiMapperBase(), StartCommentsToUiMapperBase(time), StartAlbumsToUiMapperBase(),
        StartTimesMapperBase(time), StartReviewToUiMapperBase())
    private val current = """{
        "id":309,"name":"Юрманка трейл","start_date":"2026-10-17T00:00:00.000Z",
        "end_date":"2026-10-18T10:00:00.000Z","conditionFile":null,"condition_short":null,
        "coordinates":null,"description":null,"sponsors":[],"distances":[],"distinctDistances":{},
        "organizers":[],"payment_disabled":null,"payment_type":null,"posterLinkFile":null,
        "short_name":null,"slug":"yurmanka-trejl-309","start_status":{"code":6,"name":"Старт завершён"},
        "start_time":null,"reg_link":null,"useful_links":[],"results":[]
    }"""
    private val legacyFields = """{
        "city":null,"finish_reg":null,"hashtags":null,"isClosed":null,"kindOfSports":[],"discount":[],
        "reg_on_site":false,"registration_end_date":"2026-10-16T00:00:00Z",
        "registration_start_date":"2026-01-01T00:00:00Z","season":null,"select_kinds_sport":"",
        "serial_number":null,"short_name":"Юрманка трейл","social_networks":[],"start_reg":null,
        "status":null,"viewsCount":1
    }"""

    @Test fun bothApiVersionsMapDateRangeAndFavoritesRetainIt() {
        for (old in listOf(false, true)) {
            val item = map(payload(old))
            assertEquals("17–18 октября 2026", item.startTime)
            assertEquals("17 октября 2026", item.startData)
            val card = StartItemToStartsListItemMapper.map(item)
            assertEquals(item.startTime, card.displayDate)
            assertEquals(item.startData, card.date)
            val partial = StartsListItemToStartItemMapper.mapToPartialStartItem(card)
            assertEquals(item.id, partial.id)
            // Opening cached cards must preserve the complete range before details arrive.
            assertEquals(card.dateRange, StartItemToStartsListItemMapper.map(partial).dateRange)
        }
    }

    @Test fun missingNullAndInvalidEndDatesDoNotBreakEitherResponseVersion() {
        for (old in listOf(false, true)) for (end in listOf(null, JsonNull, JsonPrimitive(""), JsonPrimitive("invalid"))) {
            val fields = payload(old) - "end_date" + if (end == null) emptyMap() else mapOf("end_date" to end)
            assertEquals("17 октября 2026", map(JsonObject(fields), old).startTime)
        }
    }

    private fun payload(old: Boolean) = JsonObject(json.parseToJsonElement(current).jsonObject +
        if (old) json.parseToJsonElement(legacyFields).jsonObject else emptyMap())

    private fun map(fields: JsonObject, old: Boolean = "city" in fields) = mapper.map(
        json.decodeFromJsonElement<StartRemote>(if (old) buildJsonObject { put("start_data", fields) } else fields),
        emptyList(), emptyList(), emptyList(), emptyList())
}
