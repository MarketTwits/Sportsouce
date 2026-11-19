package com.markettwits.sportsouce.start.cloud

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.sportsouce.start.cloud.api.start.SportSauceStartApiBase
import com.markettwits.sportsouce.start.cloud.model.comments.request.StartCommentRequest
import com.markettwits.sportsouce.start.cloud.model.comments.request.StartSubCommentRequest
import com.markettwits.sportsouce.start.cloud.model.comments.response.Comment
import com.markettwits.sportsouce.start.cloud.model.comments.response.Reply
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

class CommentsApiTest {
    private val json = JsonProviderBase().provide()
    private val user = """{"id":1,"name":"Имя","surname":"Фамилия","photo":null}"""
    private val reply = """{"id":84,"comment":"Ответ","parentCommentId":172,"personId":"1","isAdminAnswer":true,"createdAt":"2026-10-24T04:48:03.000Z","updatedAt":"2026-10-24T04:48:03.000Z","user":$user}"""
    private fun comment(extra: String = "") = """{"id":172,"comment":"Вопрос","countSub":1,"personId":"1","startId":318,"createdAt":"2026-10-24T04:47:12.000Z","updatedAt":"2026-10-24T04:47:12.000Z","user":$user$extra}"""

    @Test fun newFlagsRepliesAndNumericReviewScoreAreDecoded() = runTest {
        val response = """{"count":1,"rows":[${comment(",\"isQuestion\":true,\"isAnswered\":true,\"isReview\":false,\"averageScore\":4.5,\"replies\":[$reply]")}]}"""
        withApi(response) { api ->
            val row = api.comments(318).single()
            assertEquals(true, row.isQuestion)
            assertEquals(true, row.isAnswered)
            assertEquals("4.5", row.averageScore)
            assertEquals(true, row.replies!!.single().isAdminAnswer)
        }
    }

    @Test fun olderResponsesAndPostWithoutRepliesRemainCompatible() {
        val row = json.decodeFromString<Comment>(comment())
        assertFalse(row.isQuestion == true)
        assertFalse(row.isAnswered == true)
        assertFalse(row.isReview == true)
        assertTrue(row.replies.isNullOrEmpty())
        val oldReply = json.decodeFromString<Reply>(reply.replace(",\"isAdminAnswer\":true", ""))
        assertFalse(oldReply.isAdminAnswer == true)
        val photo = """{"fullPath":"https://cdn.example/photo.jpg","id":9,"name":"photo.jpg","path":"https://cdn.example","lastModified":null,"original_name":null}"""
        assertNotNull(json.decodeFromString<Comment>(comment().replace("\"photo\":null", "\"photo\":$photo")).user.photo)
    }

    @Test fun questionRequestUsesCurrentContractAndOnlyServerDecidesAuthorAndAnswerStatus() = runTest {
        withApi(comment(",\"isQuestion\":true,\"isAnswered\":false")) { api ->
            val result = api.writeComment(StartCommentRequest("Вопрос", "318", false, true), "test-token")
            assertEquals(true, result.isQuestion)
            assertTrue(result.replies.isNullOrEmpty())
        }
        assertEquals(setOf("comment", "startId", "isReview", "isQuestion"), lastBody.keys)
        assertEquals("318", lastBody["startId"]!!.jsonPrimitive.content)
        assertTrue(lastBody["isQuestion"]!!.jsonPrimitive.boolean)
        assertFalse(lastBody["isReview"]!!.jsonPrimitive.boolean)
        assertEquals("/comment", lastPath)
    }

    @Test fun repliesSendParentAndStartAndDecodeAdministratorFlag() = runTest {
        withApi(reply) { api ->
            assertEquals(true, api.writeSubComment(StartSubCommentRequest("Ответ", "172", "318"), "test-token").isAdminAnswer)
        }
        assertEquals("/comment/sub-comment", lastPath)
        assertEquals(setOf("comment", "parentCommentId", "startId"), lastBody.keys)
        assertEquals("172", lastBody["parentCommentId"]!!.jsonPrimitive.content)
        assertFalse("isAdminAnswer" in lastBody)
    }

    private var lastBody = JsonObject(emptyMap())
    private var lastPath = ""
    private suspend fun withApi(response: String, block: suspend (SportSauceStartApiBase) -> Unit) {
        val client = HttpClient(MockEngine { request ->
            lastPath = request.url.encodedPath
            if (request.method == HttpMethod.Post) {
                assertEquals("Bearer test-token", request.headers[HttpHeaders.Authorization])
                lastBody = json.parseToJsonElement((request.body as TextContent).text).jsonObject
            }
            respond(response, headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }) {
            expectSuccess = true
            defaultRequest { url("https://api.example.test/") }
            install(ContentNegotiation) { json(json) }
        }
        try {
            block(SportSauceStartApiBase(object : HttpClientProvider {
                override fun provide(loggerEnabled: Boolean) = client
                override fun json() = json
            }))
        } finally { client.close() }
    }
}
