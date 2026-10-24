package com.markettwits.sportsouce.profile.results

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.sportsouce.profile.results.data.*
import com.markettwits.sportsouce.profile.results.domain.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.*
import kotlin.test.*

internal class TestClient(handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData) : HttpClientProvider {
    val requests = mutableListOf<HttpRequestData>()
    private val format = Json { ignoreUnknownKeys = true; encodeDefaults = true }
    val client = HttpClient(MockEngine(MockEngineConfig().apply { dispatcher = Dispatchers.Unconfined; addHandler { request -> requests += request; handler(request) } })) {
        expectSuccess = true
        install(ContentNegotiation) { json(format) }
        defaultRequest { url("https://api.example.test/") }
    }
    override fun provide(loggerEnabled: Boolean) = client
    override fun json() = format
}
internal fun MockRequestHandleScope.jsonResponse(body: String) = respond(body, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))

class MyResultsContractTest {
    @Test fun nullableFeedFieldsDecimalsAndAllKindsDecodeWithoutInventingResults() = runTest {
        val provider = TestClient { jsonResponse("""{"count":40,"rows":[
            {"key":"protocol:1","kind":"result","source":"protocol","startId":235,"resultSeconds":2238.1,"bib":"43","groupFinishers":null,"isTeam":null,"unknown":true},
            {"key":"registration:2","kind":"upcoming","date":"2027-08-21","resultSeconds":null},
            {"key":"registration:3","kind":"awaiting"},
            {"key":"file:4","kind":"file","fileUrl":"https://cdn.example.test/protocol.pdf"},
            {"key":"external:5","kind":"pending","startId":null,"resultText":"1:42:18","comment":"Проверяем"},
            {"key":"external:6","kind":"rejected","resultSeconds":null}
        ]}""") }
        try {
            val page = MyResultsApi(provider).feed("test-token", 20, 20, "run:10000")
            assertEquals(40, page.count); assertEquals(2238.1, page.rows.first().resultSeconds)
            assertFalse(page.rows.first().isTeam)
            assertEquals("Старт впереди", page.rows[1].resultLabel())
            assertEquals("Результаты ещё не опубликованы", page.rows[2].resultLabel())
            assertEquals("Протокол в файле", page.rows[3].resultLabel())
            assertEquals("1:42:18", page.rows[4].resultLabel())
            val request = provider.requests.single()
            assertEquals("20", request.url.parameters["skipCount"])
            assertEquals("run:10000", request.url.parameters["category"])
            assertEquals("Bearer test-token", request.headers[HttpHeaders.Authorization])
        } finally { provider.client.close() }
    }

    @Test fun progressUsesServerSeriesAndOptionalCategoryAndTypedComparison() = runTest {
        val provider = TestClient { jsonResponse("""{"mode":"pace","compare":"group","points":[{"key":"p:1","date":"2025-06-15","value":223.8,"resultSeconds":2238.1,"compare":{"value":286.6,"sample":45,"label":"М2_30-39 лет"}}]}""") }
        try {
            val progress = MyResultsApi(provider).progress("test-token", null, ResultComparison.Group, "park")
            assertEquals(286.6, progress.points.single().compare?.value)
            assertEquals(45, progress.points.single().compare?.sample)
            assertEquals("park", provider.requests.single().url.parameters["series"])
            assertEquals("group", provider.requests.single().url.parameters["compare"])
            assertNull(provider.requests.single().url.parameters["category"])
        } finally { provider.client.close() }
    }

    @Test fun searchClaimAndExternalPayloadMatchTheWebsite() = runTest {
        val provider = TestClient { request -> jsonResponse(when (request.url.encodedPath) {
            "/start/autocomplete" -> """{"suggestions":[{"id":235,"name":"Парковый кросс","start_date":"2025-06-15T01:00:00.000Z"}]}"""
            "/identity/unmatched-results" -> """{"rows":[{"id":7,"name":"Иван Иванов","birthYear":1991,"result":"00:36:00","start":{"id":235,"name":"Парковый кросс"}}]}"""
            else -> "{}"
        }) }
        try {
            val api = ResultsActionsRepositoryBase(ResultsActionsApi(provider)) { "test-token" }
            assertEquals(235, api.starts("Парковый").single().id)
            assertEquals(7, api.search("Иванов", 235).single().id)
            assertEquals("Иванов", provider.requests.last().url.parameters["q"])
            assertEquals("235", provider.requests.last().url.parameters["startId"])
            api.claim(7)
            assertEquals("{\"resultId\":7}", (provider.requests.last().body as TextContent).text)
            val draft = ExternalResultDraft(" Осенний кросс ", "2026-10-20", "other", "8,5", "42:18,25", "https://example.test/result")
            assertTrue(draft.errors(false).isEmpty())
            api.add(draft.request(emptyList(), null))
            val body = Json.parseToJsonElement((provider.requests.last().body as TextContent).text).jsonObject
            assertEquals("Осенний кросс", body["title"]?.jsonPrimitive?.content)
            assertEquals("20.10.2026", body["dateText"]?.jsonPrimitive?.content)
            assertEquals("2026-10-20", body["eventDate"]?.jsonPrimitive?.content)
            assertEquals(8500.0, body["distanceM"]?.jsonPrimitive?.double)
            assertEquals("42:18.25", body["resultText"]?.jsonPrimitive?.content)
            assertEquals(HttpMethod.Post, provider.requests.last().method)
        } finally { provider.client.close() }
    }

    @Test fun proofUsesMultipartUploadAndOnlyTheReturnedFileIdIsAttached() = runTest {
        val provider = TestClient { jsonResponse("{\"id\":77,\"fullPath\":\"https://example.test/protocol.pdf\"}") }
        try {
            val api = ResultsActionsApi(provider)
            val file = ResultProofFile("protocol.pdf", "application/pdf", byteArrayOf(37, 80, 68, 70), 123)
            assertEquals(77, api.upload("test-token", file))
            assertEquals("/file/upload", provider.requests.single().url.encodedPath)
            assertTrue(provider.requests.single().body.contentType?.match(ContentType.MultiPart.FormData) == true)
            assertEquals("Bearer test-token", provider.requests.single().headers[HttpHeaders.Authorization])
        } finally { provider.client.close() }
    }

    @Test fun malformedDatesTimesAndMissingProofHaveFieldErrors() {
        val empty = ExternalResultDraft().errors(false)
        assertTrue(setOf(ResultField.Title, ResultField.Date, ResultField.Distance, ResultField.Time, ResultField.Proof).all { it in empty })
        val draft = ExternalResultDraft("Старт", "2026-02-30", "other", "NaN", "1:99:00", "javascript:alert(1)")
        assertTrue(setOf(ResultField.Date, ResultField.Distance, ResultField.Time, ResultField.Proof).all { it in draft.errors(false) })
        assertFalse(validResultTime("00:00")); assertFalse(validResultTime("1:00:60")); assertTrue(validResultTime("18:20.71"))
        assertTrue(ExternalResultDraft("Старт", "2026-10-20", "run:5000", time = "18:20").errors(true).isEmpty())
        assertEquals("18:21", resultTime(1100.71)); assertEquals("1:01:15", resultTime(3674.79))
        assertEquals("—", resultTime(Double.NaN)); assertEquals("—", resultTime(null))
        assertEquals("255 место", resultPlace(255, 128))
        assertEquals("старт", resultCountWord(21, "старт", "старта", "стартов"))
        assertEquals("стартов", resultCountWord(11, "старт", "старта", "стартов"))
    }
}
