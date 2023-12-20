package ru.alexpanov.core_network.api

import com.markettwits.cloud.model.auth.sign_in.request.SignInRequest
import com.markettwits.cloud.model.auth.sign_in.response.SignInResponse
import com.markettwits.cloud.model.start.StartRemote
import com.markettwits.cloud.model.start_comments.StartCommentsRemote
import com.markettwits.cloud.model.start_member.StartMemberItem
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.decodeFromString
import com.markettwits.cloud.model.starts.StartsRemote
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.alexpanov.core_network.provider.HttpClientProvider2

class StartsRemoteDataSourceImpl(
    private val httpClient: HttpClientProvider2
) : SportsouceApi {

    private val json = httpClient.getJson()
    private val client = httpClient.get()
    override suspend fun fetchActualStarts(): StartsRemote {
        val serializer = StartsRemote.serializer()
        val response = client.get("start")
        return json.decodeFromString(serializer, response.body<String>())
    }

    override suspend fun fetchPasteStarts(): StartsRemote {
        val serializer = StartsRemote.serializer()
        val response = client.get("start?maxResultCount=40&group=true&name=Зима+2023-2024,Лето 2023&status=0,6")
        return json.decodeFromString(serializer, response.body<String>())
    }

    override suspend fun fetchPreview(): StartsRemote {
        val serializer = StartsRemote.serializer()
        val response = client.get("start?maxResultCount=40&group=true&name=Зима+2024-2025,Лето 2024")
        return json.decodeFromString(serializer, response.body<String>())
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

    override suspend fun signIn(signInRequest: SignInRequest): SignInResponse {
        val response = client.post("authentication/log-in"){
            contentType(ContentType.Application.Json)
            setBody(signInRequest)
        }
        return json.decodeFromString(response.body())
    }
}