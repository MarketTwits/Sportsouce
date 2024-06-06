package com.markettwits.cloud.api

import com.markettwits.cloud.model.auth.reset_password.ResetPasswordResponse
import com.markettwits.cloud.model.auth.sign_in.request.SignInRequest
import com.markettwits.cloud.model.auth.sign_in.response.SignInResponseSuccess
import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.change_password.ChangePasswordRequest
import com.markettwits.cloud.model.city.CityRemote
import com.markettwits.cloud.model.image.UploadFileResponse
import com.markettwits.cloud.model.kind_of_sport.KindOfSportRemote
import com.markettwits.cloud.model.news.NewsRemote
import com.markettwits.cloud.model.profile.members.ProfileMemberRequest
import com.markettwits.cloud.model.profile.members.ProfileMembers
import com.markettwits.cloud.model.profile.update.ChangeProfileInfoRequest
import com.markettwits.cloud.model.profile.update.ChangeProfileInfoResponse
import com.markettwits.cloud.model.promocode.PromocodeRemote
import com.markettwits.cloud.model.seasons.StartSeasonsRemote
import com.markettwits.cloud.model.sign_up.SignUpRequest
import com.markettwits.cloud.model.sign_up.SignUpResponse
import com.markettwits.cloud.model.start.StartRemote
import com.markettwits.cloud.model.start_album.StartAlbumRemote
import com.markettwits.cloud.model.start_comments.request.StartCommentRequest
import com.markettwits.cloud.model.start_comments.request.StartSubCommentRequest
import com.markettwits.cloud.model.start_comments.response.CommentRow
import com.markettwits.cloud.model.start_comments.response.Reply
import com.markettwits.cloud.model.start_comments.response.StartCommentsRemote
import com.markettwits.cloud.model.start_donation.StartDonationRequest
import com.markettwits.cloud.model.start_donation.StartDonationResponse
import com.markettwits.cloud.model.start_member.StartMemberItem
import com.markettwits.cloud.model.start_registration.StartRegisterRequest
import com.markettwits.cloud.model.start_registration.StartRegistrationResponse
import com.markettwits.cloud.model.start_registration.StartRegistrationResponseWithoutPayment
import com.markettwits.cloud.model.start_user.RemoteStartsUserItem
import com.markettwits.cloud.model.starts.StartsRemote
import com.markettwits.cloud.model.team.TeamsRemote
import com.markettwits.core_cloud.provider.HttpClientProvider
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import java.io.File

internal class StartsRemoteDataSourceImpl(
    private val httpClient: HttpClientProvider
) : SportsouceApi {

    private val json = httpClient.json()
    private val client = httpClient.provide(false)
    override suspend fun teams(): TeamsRemote {
        val response = client.get("team")
        return json.decodeFromString(TeamsRemote.serializer(), response.body<String>())
    }

    override suspend fun cities(withStarts: Boolean): CityRemote {
        val response = if (withStarts) {
            client.get("city?withStarts=true")
        } else {
            client.get("city")
        }
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

    override suspend fun startWithFilter(request: Map<String, String>): StartsRemote {
        val response = client.get("start") {
            url {
                parameters.apply {
                    request.forEach { (param, value) ->
                        append(param, value)
                    }
                }
            }
        }
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun fetchActualStarts(): StartsRemote {
        val serializer = StartsRemote.serializer()
        val response = client.get("start?maxResultCount=20&group=true&status=3,2")
        return json.decodeFromString(serializer, response.body<String>())
    }

    override suspend fun fetchPasteStarts(): StartsRemote {
        val serializer = StartsRemote.serializer()
        val response =
            client.get("start?maxResultCount=1000&group=true&status=6")
        return json.decodeFromString(serializer, response.body<String>())
    }

    override suspend fun fetchPreview(): StartsRemote {
        val serializer = StartsRemote.serializer()
        val response =
            client.get("start?maxResultCount=20&group=true&status=2")
        return json.decodeFromString(serializer, response.body<String>())
    }

    override suspend fun fetchStartMain(): StartsRemote {
        val response = client.get("start?mainPage=true")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun repay(id: Int, token: String): StartRegistrationResponse {
        val response = client.put("member-start/$id/repayment") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return json.decodeFromString(response.body())
    }

    override suspend fun registerOnStartBase(
        request: StartRegisterRequest,
        token: String
    ): StartRegistrationResponse {
        val response = client.post("member-start") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(request)
        }
        return json.decodeFromString(StartRegistrationResponse.serializer(), response.body())
    }

    override suspend fun registerOnStartCombo(
        request: StartRegisterRequest.Combo,
        token: String
    ): StartRegistrationResponse {
        val response = client.post("member-start/combo") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(request)
        }
        return json.decodeFromString(response.body())
    }

    @Deprecated("dont use")
    override suspend fun registerOnStartWithoutPayment(
        request: StartRegisterRequest,
        token: String
    ): StartRegistrationResponseWithoutPayment {
        val response = client.post("member-start") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(request)
        }
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun promo(value: String, startId: Int): PromocodeRemote {
        val response = client.get("start/$startId/promocode?code=$value")
        return json.decodeFromString(response.body<String>())
    }

    override suspend fun fetchStart(startId: Int): StartRemote {
        val serializer = StartRemote.serializer()
        val response = client.get("start/$startId")
        return json.decodeFromString(serializer, response.body<String>())
    }

    override suspend fun fetchStartAlbum(startId: Int): StartAlbumRemote {
        val response = client.get("album") {
            url {
                parameters.append("start_id", startId.toString())
            }
        }
        return json.decodeFromString(response.body<String>())
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

    override suspend fun uploadFile(file: File): UploadFileResponse {
        val response = client.submitFormWithBinaryData(
            url = "file/upload",
            formData = formData {
                append("file", file.readBytes(), Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                })
                append("lastModified", file.lastModified())
            }
        )
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

    override suspend fun changePassword(
        password: ChangePasswordRequest,
        token: String
    ): ChangeProfileInfoResponse {
        val response = client.put("authentication/change-password") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(password)
        }
        return json.decodeFromString(response.body())
    }

    override suspend fun userRegistries(userId: Int, token: String): List<RemoteStartsUserItem> {
        val response = client.get("user/startsByUserId/$userId") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return json.decodeFromString(response.body())
    }

    override suspend fun memberTemplate(userId: Int, token: String): ProfileMembers {
        val response = client.get("member-template?user_id=$userId") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return json.decodeFromString(response.body())
    }

    override suspend fun deleteMember(memberId: Int, token: String) {
        val response = client.delete("member-template/$memberId") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return json.decodeFromString(response.body())
    }

    override suspend fun updateMember(memberId: Int, request: ProfileMemberRequest, token: String) {
        val response = client.put("member-template/$memberId") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(request)
        }
        return json.decodeFromString(response.body())
    }

    override suspend fun addMember(request: ProfileMemberRequest, token: String) {
        val response = client.post("member-template") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(request)
        }
        return json.decodeFromString(response.body())
    }

    override suspend fun news(): NewsRemote {
        val response = client.get("news")
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

    override suspend fun donation(startDonationRequest: StartDonationRequest): StartDonationResponse {
        val response = client.post("donations") {
            contentType(ContentType.Application.Json)
            setBody(startDonationRequest)
        }
        return json.decodeFromString(response.body())
    }

    override suspend fun resetPassword(email: String): ResetPasswordResponse {
        val response = client.post("authentication/reset-password") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("email" to email))
        }
        return json.decodeFromString(response.body())
    }

    override suspend fun register(signUpRequest: SignUpRequest): SignUpResponse {
        val response = client.post("authentication/register") {
            contentType(ContentType.Application.Json)
            setBody(signUpRequest)
        }
        return json.decodeFromString(response.body())
    }
}