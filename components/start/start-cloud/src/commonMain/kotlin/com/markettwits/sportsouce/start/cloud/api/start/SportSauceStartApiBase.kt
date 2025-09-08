package com.markettwits.sportsouce.start.cloud.api.start

import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.sportsouce.start.cloud.model.comments.request.StartCommentRequest
import com.markettwits.sportsouce.start.cloud.model.comments.request.StartSubCommentRequest
import com.markettwits.sportsouce.start.cloud.model.comments.response.Comment
import com.markettwits.sportsouce.start.cloud.model.comments.response.Reply
import com.markettwits.sportsouce.start.cloud.model.comments.response.StartCommentsRemote
import com.markettwits.sportsouce.start.cloud.model.donation.StartDonationRequest
import com.markettwits.sportsouce.start.cloud.model.donation.StartDonationResponse
import com.markettwits.sportsouce.start.cloud.model.filters.FiltersRemote
import com.markettwits.sportsouce.start.cloud.model.kindofsport.KindOfSportRemote
import com.markettwits.sportsouce.start.cloud.model.members.StartMember
import com.markettwits.sportsouce.start.cloud.model.members.StartMembersRemote
import com.markettwits.sportsouce.start.cloud.model.result.v1.StartMemberResultRowsV1
import com.markettwits.sportsouce.start.cloud.model.result.v1.StartMemberResultV1
import com.markettwits.sportsouce.start.cloud.model.result.v2.StartMembersResultRowsV2
import com.markettwits.sportsouce.start.cloud.model.seasons.StartSeasonsRemote
import com.markettwits.sportsouce.start.cloud.model.start.StartRemote
import com.markettwits.sportsouce.start.cloud.model.start.fields.album.StartAlbum
import com.markettwits.sportsouce.start.cloud.model.start.fields.album.StartAlbumRemote
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

internal class SportSauceStartApiBase(
    private val httpClient: HttpClientProvider
) : SportSauceStartApi {

    private val json = httpClient.json()

    private val client = httpClient.provide(true)

    override suspend fun start(startId: String): StartRemote {
        val response = client.get("start/$startId")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun kindOfSports(): KindOfSportRemote {
        val response = client.get("kind-of-sport")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun seasons(): StartSeasonsRemote {
        val response = client.get("season")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun filters(startId: Int): FiltersRemote {
        val response = client.get("member-start/$startId/filters") {
            parameter("start_id", startId)
        }
        return json.decodeFromString(response.body<String>())
    }

    @Deprecated("Use membersFiltered(startId, ...) with server-side filtering and sorting")
    override suspend fun members(startId: Int): List<StartMember> {
        val response = client.get("member-start/paid") {
            parameter("start_id", startId)
            parameter("maxResultCount", 1000)
        }
        return json.decodeFromString<StartMembersRemote>(response.body<String>()).rows
    }

    override suspend fun membersFiltered(
        startId: Int,
        filterText: String,
        skipCount: Int,
        maxResultCount: Int,
        distances: List<Int>,
        genders: List<String>,
        sorting: String?,
    ): StartMembersRemote {
        val response = client.get("member-start/paid") {
            parameter("start_id", startId)
            parameter("skipCount", skipCount)
            parameter("maxResultCount", maxResultCount)
            if (filterText.isNotBlank()) parameter("filterText", filterText)
            distances.forEach { parameter("distances[]", it) }
            genders.forEach { parameter("genders[]", it) }
            if (!sorting.isNullOrBlank()) parameter("sorting", sorting)
        }
        return json.decodeFromString<StartMembersRemote>(response.body<String>())
    }

    override suspend fun membersResults(
        maxResultCount: Int,
        startId: Int,
        filterText: String,
        sorting: String,
    ): List<StartMemberResultV1> {
        val response = client.get("member-result") {
            parameter("maxResultCount", maxResultCount.toString())
            parameter("start_id", startId.toString())
            parameter("sorting", sorting)
            if (filterText.isNotBlank()) {
                parameter("filterText", filterText)
            }
        }
        return json.decodeFromString<StartMemberResultRowsV1>(response.body()).rows
    }

    override suspend fun membersResultsAnalyze(
        startId: Int,
        page: Int,
        maxResultCount: Int,
        gender: String,
        group: String,
        distance: String,
    ): StartMembersResultRowsV2 {
        val response = client.get("race/analyze-results/$startId") {
            parameter("page", page)
            parameter("maxResultCount", maxResultCount)
            if (gender.isNotBlank()) parameter("gender", gender)
            if (group.isNotBlank()) parameter("group", group)
            if (distance.isNotBlank()) parameter("distance", distance)
        }
        return json.decodeFromString<StartMembersResultRowsV2>(response.body<String>())
    }

    override suspend fun donation(startDonationRequest: StartDonationRequest): StartDonationResponse {
        val response = client.post("donations") {
            contentType(ContentType.Application.Json)
            setBody(startDonationRequest)
        }
        return json.decodeFromString(response.body())
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