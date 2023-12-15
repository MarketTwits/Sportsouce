package ru.alexpanov.core_network.api

import com.markettwits.cloud.model.start.StartRemote
import ru.alexpanov.core_network.model.all.StartsRemote
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.alexpanov.core_network.provider.HttpClientProvider2

class StartsRemoteDataSourceImpl(
    private val httpClient: HttpClientProvider2
) : SportsouceApi {

    private val json = httpClient.getJson()
    private val client = httpClient.get()
    override suspend fun fetchStarts(): StartsRemote {
        val serializer = StartsRemote.serializer()
        val response = client.get("start")
        return json.decodeFromString(serializer, response.body<String>())
    }

    override suspend fun fetchStartsWithFilter(): StartsRemote {
        val serializer = StartsRemote.serializer()
        val response = client.get("start?skipCount=6&maxResultCount=6&group=true&name=%D0%9B%D0%B5%D1%82%D0%BE+2024,%D0%9B%D0%B5%D1%82%D0%BE+2023&year=&kindOfSports=&city=&fromDate=&toDate=&status=0,6")
        return json.decodeFromString(serializer, response.body<String>())
    }

    override suspend fun fetchStart(startId: Int): StartRemote {
        val serializer = StartRemote.serializer()
        val response = client.get("start/$startId")
        return json.decodeFromString(serializer, response.body<String>())
    }
}