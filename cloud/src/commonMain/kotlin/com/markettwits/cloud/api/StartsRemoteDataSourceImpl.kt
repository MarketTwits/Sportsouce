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
import com.markettwits.cloud.model.seasons.StartSeasonsRemote
import com.markettwits.cloud.model.sign_up.SignUpRequest
import com.markettwits.cloud.model.sign_up.SignUpResponse
import com.markettwits.cloud.model.start_donation.StartDonationRequest
import com.markettwits.cloud.model.start_donation.StartDonationResponse
import com.markettwits.cloud.model.start_price.StartPriceRequest
import com.markettwits.cloud.model.start_price.StartPriceResponse
import com.markettwits.cloud.model.start_registration.StartRegistrationResponse
import com.markettwits.cloud.model.start_user.values.UserRegistration
import com.markettwits.cloud.model.start_user.values.UserRegistrationsRemote
import com.markettwits.cloud.model.starts.StartsRemote
import com.markettwits.cloud.model.team.TeamsRemote
import com.markettwits.core_cloud.provider.HttpClientProvider
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.*

internal class StartsRemoteDataSourceImpl(
    private val httpClient: HttpClientProvider
) : SportsouceApi {

    private val json = httpClient.json()

    private val client = httpClient.provide(true)

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
        val response = client.get("start?mainPage=true&openFirst=true")
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

    override suspend fun checkStartPrice(
        startPriceRequest: StartPriceRequest,
        id: Int,
        token: String
    ): StartPriceResponse {
        val response = client.post("member-start/$id/price") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            setBody(startPriceRequest)
        }
        return json.decodeFromString(StartPriceResponse.serializer(), response.body())
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

    override suspend fun uploadFile(data: ByteArray, lastModified: Long): UploadFileResponse {
        val response = client.submitFormWithBinaryData(
            url = "file/upload",
            formData = formData {
                append("file", data, Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=uploaded_file")
                })
                append("lastModified", lastModified.toString())
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

    override suspend fun userRegistriesNew2(userId: Int, token: String): List<UserRegistration> {
        val response = client.get("user/startsByUserId/$userId?maxResultCount=1000") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }
        return json.decodeFromString<UserRegistrationsRemote>(response.body()).rows
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