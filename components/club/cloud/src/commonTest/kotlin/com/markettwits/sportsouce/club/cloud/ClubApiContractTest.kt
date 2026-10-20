package com.markettwits.sportsouce.club.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.sportsouce.club.cloud.api.SportSauceClubsNetworkApiBase
import com.markettwits.sportsouce.club.cloud.models.workout.registration.WorkoutRegistrationRequestRemote
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.http.content.TextContent
import kotlinx.serialization.json.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class ClubApiContractTest {
    @Test fun rejectedWorkoutRequestRemainsAFailure() = runTest {
        val json = JsonProviderBase().provide()
        val client = HttpClient(MockEngine {
            respond("""{"statusCode":400,"message":"Заявка отклонена"}""", status = HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }) {
            expectSuccess = true
            install(ContentNegotiation) { json(json) }
            defaultRequest { url("https://api.sportsauce.ru/") }
        }
        try {
            val api = SportSauceClubsNetworkApiBase(object : HttpClientProvider {
                override fun provide(loggerEnabled: Boolean) = client
                override fun json() = json
            })
            assertFailsWith<ClientRequestException> {
                api.workoutRequest(WorkoutRegistrationRequestRemote(9, "workout", name = "Даниил", surname = "Кулаков", phone = "79130037675"))
            }
        } finally { client.close() }
    }

    @Test fun workoutRequestsUseTheWebsiteContractAndAcceptCreatedRecords() = runTest {
        val json = JsonProviderBase().provide()
        val requests = listOf(
            WorkoutRegistrationRequestRemote(16, "subscription", 2, "Даниил", "Кулаков", "79130037675"),
            WorkoutRegistrationRequestRemote(9, "workout", name = "Даниил", surname = "Кулаков", phone = "79130037675"),
            WorkoutRegistrationRequestRemote(10, "trainer", name = "Даниил", surname = "Кулаков", phone = "79130037675")
        )
        var calls = 0
        val client = HttpClient(MockEngine { request ->
            val expected = requests[calls++]
            assertEquals(HttpMethod.Post, request.method)
            assertEquals("/workout-request", request.url.encodedPath)
            val payload = json.parseToJsonElement((request.body as TextContent).text).jsonObject
            assertEquals(JsonPrimitive(expected.id), payload["id"])
            assertEquals(JsonPrimitive(expected.type), payload["type"])
            assertEquals(JsonPrimitive(expected.name), payload["name"])
            assertEquals(JsonPrimitive(expected.surname), payload["surname"])
            assertEquals(JsonPrimitive(expected.phone), payload["phone"])
            if (expected.count != null) assertEquals(JsonPrimitive(2), payload["count"])
            else assertNull(payload["count"])
            respond("""{"status":"Новая заявка","id":107,"${expected.type}Id":${expected.id},"name":"Даниил","surname":"Кулаков","phone":"79130037675"${if (expected.count != null) ",\"count\":2" else ""},"updatedAt":"2026-10-20T14:35:33.494Z","createdAt":"2026-10-20T14:35:33.494Z"}""",
                status = HttpStatusCode.Created, headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }) {
            expectSuccess = true
            install(ContentNegotiation) { json(json) }
            defaultRequest { url("https://api.sportsauce.ru/") }
        }
        try {
            val api = SportSauceClubsNetworkApiBase(object : HttpClientProvider {
                override fun provide(loggerEnabled: Boolean) = client
                override fun json() = json
            })
            requests.forEach { api.workoutRequest(it) }
            assertEquals(3, calls)
        } finally { client.close() }
    }

    @Test fun currentResponsesDecodeIncludingNullProcessNamesGroupedSubscriptionsAndPartialFiles() = runTest {
        val requested = mutableListOf<String>()
        val json = JsonProviderBase().provide()
        val client = HttpClient(MockEngine { request ->
            val path = request.url.encodedPath; requested += path
            if (path != "/subscription/grouped") assertEquals("100", request.url.parameters["maxResultCount"])
            if (path in listOf("/trainer", "/workout", "/club-settings")) assertEquals("order ASC", request.url.parameters["sorting"])
            if (path == "/schedule") assertNull(request.url.parameters["workoutId"])
            respond(when (path) {
                "/club-settings" -> """{"count":2,"rows":[{"id":14,"key":"process","name":null,"description":"<p>Выберем цель</p>","image":null,"imageId":null,"createdAt":"2024-01-01","order":1},{"id":1,"key":"main_image","name":"Клуб","description":null,"imageId":null,"createdAt":"2024-01-01","image":{"id":1,"name":"club.jpg","path":"https://cdn.example.test/club"}}]}"""
                "/subscription/grouped" -> """[{"id":13,"type":"Беговые тренировки","isHidden":false,"subscriptions":[{"id":11,"name":"Беговой на месяц","type":"Группа","description":"<p>Количество занятий</p>","price":3000,"discount":null,"color":"green","maxAmount":2,"kindOfSportId":9,"isPriseDependsOnCount":true,"createdAt":"2024-01-01","updatedAt":"2024-01-01","order":1,"lessonsCount":null,"validityDays":null,"discounts":[]}]},{"id":9,"type":"Функциональный тренинг","subscriptions":[]}]"""
                "/workout" -> """{"count":1,"rows":[{"id":13,"type":"Беговые тренировки","description":"<p>Бегаем вместе</p>","fileId":null,"icon":null,"order":1}]}"""
                "/trainer" -> """{"count":1,"rows":[{"id":1,"name":"Анна","surname":"Иванова","description":"Тренер","inst":null,"vk":null,"telegram":null,"facebook":null,"twitter":null,"createdAt":"2024-01-01","updatedAt":"2024-01-01","photo":null,"file_id":null,"isAcceptingRequests":false,"kindOfSports":[{"id":9,"name":"бег"}]}]}"""
                "/schedule" -> """{"count":1,"rows":[{"id":49,"description":"<p>Техника бега</p>","startDate":"09.00","address":"Манеж","weekday":"Суббота","note":"Уточните место","isTempDisabled":true,"order":15,"file_id":null,"workout_id":13,"kindOfSportId":9,"createdAt":"2024-01-01","updatedAt":"2024-01-01","trainers":[],"workout":{"id":13,"type":"Беговые тренировки","description":"Бег"},"kindOfSport":{"id":9,"name":"бег"}}]}"""
                else -> error("Unexpected endpoint $path")
            }, headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }) { expectSuccess = true; defaultRequest { url("https://api.sportsauce.ru/") } }
        val api = SportSauceClubsNetworkApiBase(object : HttpClientProvider {
            override fun provide(loggerEnabled: Boolean) = client
            override fun json() = json
        })
        try {
            val settings = api.clubSettings()
            assertNull(settings.first().name)
            assertEquals("https://cdn.example.test/club/club.jpg", settings.last().image!!.url)
            val groups = api.subscription()
            assertEquals("Беговые тренировки", groups.first().type)
            assertEquals(3000, groups.first().subscriptions!!.single().price)
            assertTrue(groups.last().subscriptions!!.isEmpty())
            assertNull(api.workout().single().icon)
            assertFalse(api.trainers().single().isAcceptingRequests)
            val schedule = api.schedule().single()
            assertEquals(13, schedule.workoutId); assertTrue(schedule.isTempDisabled)
            assertEquals("Уточните место", schedule.note)
            assertEquals(5, requested.size)
        } finally { client.close() }
    }
}
