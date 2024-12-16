package com.markettwits.start_cloud.api.start

import com.markettwits.cloud.model.start_comments.response.Reply
import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.start_cloud.model.comments.request.StartCommentRequest
import com.markettwits.start_cloud.model.comments.request.StartSubCommentRequest
import com.markettwits.start_cloud.model.comments.response.Comment
import com.markettwits.start_cloud.model.comments.response.StartCommentsRemote
import com.markettwits.start_cloud.model.filters.FiltersRemote
import com.markettwits.start_cloud.model.members.StartMember
import com.markettwits.start_cloud.model.members.StartMembersRemote
import com.markettwits.start_cloud.model.start.StartRemote
import com.markettwits.start_cloud.model.start.fields.album.StartAlbum
import com.markettwits.start_cloud.model.start.fields.album.StartAlbumRemote
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

internal class SportSauceStartApiBase(
    private val httpClient: HttpClientProvider
) : SportSauceStartApi {

    private val json = httpClient.json()

    private val client = httpClient.provide(true)

    override suspend fun start(startId : Int) : StartRemote {
        val response = client.get("start/$startId")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun filters(startId: Int): FiltersRemote {
        val response = client.get("member-start/$startId/filters")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun membersNew(startId: Int): List<StartMember> {
        val response = client.get("member-start/paid"){
            parameter("start_id", startId)
        }
        return json.decodeFromString<StartMembersRemote>(response.body<String>()).rows
    }

    override suspend fun comments(startId: Int): List<Comment> {
        val response = client.get("comment/start/$startId")
        return json.decodeFromString<StartCommentsRemote>(response.body()).rows
    }

    override suspend fun writeComment(
        startCommentRequest: StartCommentRequest,
        token: String
    ): Comment {
        val response = client.post("comment") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(startCommentRequest)
        }
        return json.decodeFromString(response.body())
    }

    override suspend fun writeSubComment(
        subComment: StartSubCommentRequest,
        token: String
    ): Reply {
        val response = client.post("comment/sub-comment") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(subComment)
        }
        return json.decodeFromString(response.body())
    }

    override suspend fun albums(startId: Int): List<StartAlbum> {
        val response = client.get("album") {
            url {
                parameters.append("start_id", startId.toString())
            }
        }
        return json.decodeFromString<StartAlbumRemote>(response.body<String>()).rows
    }

}