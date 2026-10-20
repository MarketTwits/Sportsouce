package com.markettwits.sportsouce.profile.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.sportsouce.profile.cloud.model.identity.*
import com.markettwits.sportsouce.profile.cloud.model.members.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.*
import io.ktor.http.content.TextContent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.*
import kotlin.test.*

class MembersApiContractTest {
    private val json = JsonProviderBase().provide()

    @Test fun listRequestsTheUserAnd500RowsAndToleratesNullableLegacyData() = runTest {
        val provider = provider { request ->
            assertEquals("/member-template", request.url.encodedPath)
            assertEquals("93", request.url.parameters["user_id"])
            assertEquals("500", request.url.parameters["maxResultCount"])
            assertEquals("Bearer test-token", request.headers[HttpHeaders.Authorization])
            respond("""{"count":1,"rows":[{"id":14,"name":"Анна","surname":"Иванова","child":null,"type":null,"birthday":"1986-03-02T17:00:00.000Z","user_id":93,"participant_user_id":null,"city_id":null,"user":{"id":93,"role":"admin"}}]}""")
        }
        val result = SportSauceNetworkProfileApi(provider).memberTemplate(93, "test-token")
        assertEquals(1, result.count); assertNull(result.rows.single().child); assertEquals(93, result.rows.single().userId)
    }

    @Test fun detailDoesNotRequireTheOwnerFieldAndKeepsIdentityAndSocialFields() = runTest {
        val provider = provider { request ->
            assertEquals("/member-template/2957", request.url.encodedPath)
            respond("""{"id":2957,"name":"Константин","birthday":"2005-12-01","type":"Тренер","child":false,"participant_user_id":1282,"city_id":15,"telegram":"@coach","vk":null}""")
        }
        val result = SportSauceNetworkProfileApi(provider).memberTemplateDetail(2957, "test-token")
        assertNull(result.userId); assertEquals(1282, result.participantUserId); assertEquals("@coach", result.telegram)
    }

    @Test fun putUsesTheExactMutableContractAndReadsSuccessEnvelope() = runTest {
        val provider = provider { request ->
            assertEquals(HttpMethod.Put, request.method); assertEquals("/member-template/2957", request.url.encodedPath)
            val body = json.parseToJsonElement((request.body as TextContent).text).jsonObject
            assertEquals(setOf("type", "child", "name", "surname", "gender", "birthday", "team", "phone", "email"), body.keys)
            assertEquals("2005-12-01", body["birthday"]?.jsonPrimitive?.content)
            assertEquals("79000000000", body["phone"]?.jsonPrimitive?.content)
            respond("""{"message":"Пользователь успешно обновлён","success":true}""")
        }
        SportSauceNetworkProfileApi(provider).updateMember(2957, request(), "test-token")
    }

    @Test fun rejectedMutationIsNotReportedAsSuccessAndEmptyDeleteIsAllowed() = runTest {
        val provider = provider { request ->
            if (request.method == HttpMethod.Delete) respond("", HttpStatusCode.NoContent)
            else respond("""{"success":false,"message":"Изменения запрещены"}""")
        }
        val api = SportSauceNetworkProfileApi(provider)
        val failure = runCatching { api.updateMember(2957, request(), "test-token") }.exceptionOrNull()
        assertEquals("Изменения запрещены", failure?.message)
        api.deleteMember(2957, "test-token")
    }

    @Test fun identitySearchAlwaysIncludesContextAndParsesAmbiguousEmptyAndUniqueResponses() = runTest {
        for (rows in listOf("[]", """[{"user":{"id":1282,"name":"Константин ","surname":"Маняшин","birthYear":2005,"city":null,"isGrey":false,"accountType":"regular"},"confidenceLevel":"MEDIUM","displayScore":50,"reasons":[{"code":"name_typo","label":"В имени возможна опечатка","positive":false}]}]""")) {
            val provider = provider { request ->
                assertEquals(HttpMethod.Post, request.method); assertEquals("/identity/registration-preview", request.url.encodedPath)
                val body = json.parseToJsonElement((request.body as TextContent).text).jsonObject
                assertEquals("THIRD_PARTY_REGISTRATION", body["context"]?.jsonPrimitive?.content)
                assertEquals(2005, body["birthYear"]?.jsonPrimitive?.int)
                respond("""{"context":"THIRD_PARTY_REGISTRATION","unique":false,"needsClarification":false,"message":"Поиск завершён","clarificationHints":[],"rows":$rows,"subject":null}""")
            }
            val result = SportSauceNetworkIdentityApi(provider).preview(IdentityPreviewRequest("Маняшин", "Констанин", 2005, "THIRD_PARTY_REGISTRATION"), "test-token")
            assertEquals(if (rows == "[]") 0 else 1, result.rows.size)
        }
        val result = json.decodeFromString<IdentityRegistrationPreview>("""{"unique":true,"subject":{"id":8640},"rows":[{"user":{"id":8640,"name":"Анна","surname":"Иванова"},"confidenceLevel":"HIGH"}]}""")
        assertTrue(result.unique); assertEquals(8640, result.subject?.id)
    }

    @Test fun teamRequestUsesTargetUserIdAndTeamLoadsBothDirectionsAndHistory() = runTest {
        val provider = provider { request ->
            when (request.method) {
                HttpMethod.Post -> {
                    assertEquals("/identity/team/requests", request.url.encodedPath)
                    assertEquals("""{"targetUserId":1282}""", (request.body as TextContent).text)
                    respond("""{"id":5,"status":"pending","comment":null,"requester":{"id":2310},"target":{"id":1282},"createdAt":"2026-10-19T15:17:39.222Z"}""")
                }
                else -> {
                    assertEquals("/identity/team", request.url.encodedPath)
                    respond("""{"trusted":[{"id":1,"trustedUser":{"id":92,"name":"Николай","surname":"Иванов"},"ownerUser":{"id":93},"autoAcceptRegistrations":true}],"myTeam":[],"requests":{"incoming":[{"id":1,"status":"accepted","requester":{"id":92},"target":{"id":93}}],"outgoing":[{"id":2,"status":"pending","requester":{"id":93},"target":{"id":92}}]},"relations":{"92":"relative"}}""")
                }
            }
        }
        val api = SportSauceNetworkIdentityApi(provider)
        assertEquals("pending", api.requestConnection(1282, "test-token").status)
        val team = api.team("test-token")
        assertTrue(team.trusted.single().autoAcceptRegistrations == true)
        assertEquals("accepted", team.requests.incoming.single().status)
        assertEquals("pending", team.requests.outgoing.single().status)
        assertEquals("relative", team.relations["92"])
    }

    @Test fun connectionActionsUseRequestIdsTrustIdsAndThePublishedBodies() = runTest {
        val paths = mutableListOf<String>()
        val provider = provider { request ->
            paths += request.url.encodedPath
            val body = json.parseToJsonElement((request.body as TextContent).text).jsonObject
            when (request.url.encodedPath) {
                "/identity/team/requests/6/accept", "/identity/team/requests/6/reject" -> {
                    assertEquals(HttpMethod.Post, request.method); assertTrue(body.isEmpty())
                    respond("""{"id":6,"status":"accepted","requester":{"id":2310},"target":{"id":1775}}""")
                }
                "/identity/trusted/4" -> {
                    assertEquals(HttpMethod.Patch, request.method)
                    assertEquals(false, body["autoAcceptRegistrations"]?.jsonPrimitive?.boolean)
                    respond("""{"id":4,"owner_user_id":1775,"trusted_user_id":2310,"auto_accept_registrations":false}""")
                }
                "/identity/team/people/1282/relation" -> {
                    assertEquals(HttpMethod.Put, request.method); assertEquals(JsonNull, body["relation"])
                    respond("""{"success":true}""")
                }
                "/identity/trusted" -> {
                    assertEquals(HttpMethod.Post, request.method); assertEquals(1282, body["trustedUserId"]?.jsonPrimitive?.int)
                    respond("""{"id":4}""")
                }
                else -> error("Unexpected request")
            }
        }
        val api = SportSauceNetworkIdentityApi(provider)
        api.respondToRequest(6, true, "test-token"); api.respondToRequest(6, false, "test-token")
        api.updateRelation(1282, null, "test-token"); api.updateAutoAccept(4, false, "test-token"); api.addTrustedPerson(1282, "test-token")
        assertEquals(5, paths.size)
    }

    @Test fun birthYearMayBeAStringAndOptionalProfileIdIsSentAsANumber() = runTest {
        val provider = provider { request ->
            val body = json.parseToJsonElement((request.body as TextContent).text).jsonObject
            assertEquals(1282, body["profileId"]?.jsonPrimitive?.int)
            respond("""{"message":null,"rows":[{"user":{"id":1282,"name":null,"surname":null,"birthYear":"2005","isGrey":null}}]}""")
        }
        val result = SportSauceNetworkIdentityApi(provider).preview(IdentityPreviewRequest("Иванов", "Иван", 2005, "THIRD_PARTY_REGISTRATION", 1282), "test-token")
        assertEquals(2005, result.rows.single().user.birthYear)
        assertNull(result.rows.single().user.name)
    }

    @Test fun myTeamPreservesTheFullOwnerProfileEvenWithoutAutomaticAcceptance() = runTest {
        val api = SportSauceNetworkIdentityApi(provider { request ->
            assertEquals(HttpMethod.Get, request.method)
            assertEquals("/identity/team", request.url.encodedPath)
            respond("""{"trusted":[],"myTeam":[{"id":4,"trustedUser":{"id":2310},"ownerUser":{"id":1775,"name":"Дмитрий","surname":"Сергеев","patronymic":null,"birthday":"1972-01-17T17:00:00.000Z","birthYear":1972,"sex":"Мужской","city":"Новосибирск","team":"СпортСоюз","email":"runner@example.test","phone":"79000000001"},"autoAcceptRegistrations":false}],"requests":{"incoming":[],"outgoing":[]},"relations":{}}""")
        })
        val entry = api.team("test-token").myTeam.single()
        assertEquals(false, entry.autoAcceptRegistrations)
        val owner = entry.ownerUser!!
        assertEquals(1775, owner.id); assertEquals("1972-01-17T17:00:00.000Z", owner.birthday)
        assertEquals("Мужской", owner.sex); assertEquals("Новосибирск", owner.city)
        assertEquals("СпортСоюз", owner.team); assertEquals("79000000001", owner.phone)
        assertEquals("runner@example.test", owner.email); assertNull(owner.patronymic)
    }

    private fun request() = ProfileMemberRequest("2005-12-01", false, "runner@example.test", "Мужской", name = "Константин", phone = "79000000000",
        surname = "Маняшин", team = "СпортСоюз", type = "Тренер")
    private fun provider(handler: suspend MockRequestHandleScope.(io.ktor.client.request.HttpRequestData) -> io.ktor.client.request.HttpResponseData): HttpClientProvider {
        val client = HttpClient(MockEngine(handler)) {
            expectSuccess = true
            install(ContentNegotiation) { json(json) }
            defaultRequest { url("https://api.sportsauce.ru/") }
        }
        return object : HttpClientProvider {
            override fun provide(loggerEnabled: Boolean) = client
            override fun json() = json
        }
    }
}
