package ru.alexpanov.core_network.api

import com.markettwits.cloud.model.auth.common.AuthErrorResponse
import com.markettwits.cloud.model.auth.common.AuthException
import com.markettwits.cloud.model.auth.sign_in.request.SignInRequest
import com.markettwits.cloud.model.auth.sign_in.response.SignInResponseSuccess
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.change_password.ChangePasswordRequest
import com.markettwits.cloud.model.city.CityRemote
import com.markettwits.cloud.model.profile.ChangeProfileInfoRequest
import com.markettwits.cloud.model.profile.ChangeProfileInfoResponse
import com.markettwits.cloud.model.start.StartRemote
import com.markettwits.cloud.model.start_comments.request.StartCommentRequest
import com.markettwits.cloud.model.start_comments.request.StartSubCommentRequest
import com.markettwits.cloud.model.start_comments.response.CommentRow
import com.markettwits.cloud.model.start_comments.response.Reply
import com.markettwits.cloud.model.start_comments.response.StartCommentsRemote
import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.cloud.model.start_user.RemouteStartsUserItem
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.decodeFromString
import com.markettwits.cloud.model.starts.StartsRemote
import com.markettwits.cloud.model.team.TeamsRemote
import com.markettwits.sportsourcedemo.all.Row
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.path
import ru.alexpanov.core_network.provider.HttpClientProvider2

class StartsRemoteDataSourceImpl(
    private val httpClient: HttpClientProvider2
) : SportsouceApi {

    private val json = httpClient.getJson()
    private val client = httpClient.get()
    override suspend fun teams(): TeamsRemote {
        val response = client.get("team")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun cities(): CityRemote {
        val response = client.get("city")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun fetchActualStarts(): StartsRemote {
        val serializer = StartsRemote.serializer()
        val response = client.get("start")
        return json.decodeFromString(serializer, response.body<String>())
    }

    override suspend fun fetchPasteStarts(): StartsRemote {
        val serializer = StartsRemote.serializer()
        val response =
            client.get("start?maxResultCount=40&group=true&name=Зима+2023-2024,Лето 2023&status=0,6")
        return json.decodeFromString(serializer, response.body<String>())
    }

    override suspend fun fetchPreview(): StartsRemote {
        val serializer = StartsRemote.serializer()
        val response =
            client.get("start?maxResultCount=40&group=true&name=Зима+2024-2025,Лето 2024")
        return json.decodeFromString(serializer, response.body<String>())
    }

    override suspend fun fetchStartMain(): StartsRemote {
        val response = client.get("start?mainPage=true")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun fetchStart(startId: Int): StartRemote {
        val serializer = StartRemote.serializer()
        val response = client.get("start/$startId")
        return json.decodeFromString(serializer, response.body<String>())
    }

    override suspend fun fetchStartMember(startId: Int): List<StartMemberItem> {
        val response = client.get("member-start") {
            url {
                parameters.append("start_id", startId.toString())
            }
        }
        return json.decodeFromString<List<StartMemberItem>>(response.body<String>())
    }

    override suspend fun fetchStartComments(startId: Int): StartCommentsRemote {
        val response = client.get("comment/start/$startId")
        return json.decodeFromString(response.body())
    }

    override suspend fun writeComment(
        startCommentRequest: StartCommentRequest,
        token: String
    ): CommentRow {
        val response = client.post("comment") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(startCommentRequest)
        }
        return json.decodeFromString(response.body())
    }

    override suspend fun signIn(signInRequest: SignInRequest): SignInResponseSuccess {
        val response = client.post("authentication/log-in") {
            contentType(ContentType.Application.Json)
            setBody(signInRequest)
        }
        return json.decodeFromString(response.body())
    }

    override suspend fun auth(token: String): User {
        val response = client.get("authentication") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return json.decodeFromString(response.body())
    }

    override suspend fun changeProfileInfo(
        profile: ChangeProfileInfoRequest,
        token: String
    ): ChangeProfileInfoResponse {
        val response = client.put("user/${profile.id}") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(profile)
        }
        return json.decodeFromString(response.body())
    }

    override suspend fun changePassword(password: ChangePasswordRequest, token : String): ChangeProfileInfoResponse {
        val response = client.put("authentication/change-password") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(password)
        }
        return json.decodeFromString(response.body())
    }

    override suspend fun userRegistries(userId: Int, token: String) : List<RemouteStartsUserItem> {
        val response = client.get("user/startsByUserId/$userId") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return json.decodeFromString(response.body())
    }

    override suspend fun writeSubComment(
        startSubCommentRequest: StartSubCommentRequest,
        token: String
    ): Reply {
        val response = client.post("comment/sub-comment") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(startSubCommentRequest)
        }
        return json.decodeFromString(response.body())
    }
}