package com.markettwits.sportsouce.teams_city.data

import com.markettwits.cahce.InMemoryCache
import com.markettwits.cahce.execute.base.ExecuteWithCacheBase
import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.sportsouce.teams_city.data.cache.TeamsAndCities
import com.markettwits.sportsouce.teams_city.data.network.SportSauceNetworkTeamsCityApi
import com.markettwits.sportsouce.teams_city.domain.City
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TeamsCityRepositoryTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun startCitiesUseTheServerFilterEvenWhenTheFullDirectoryIsCached() = runTest {
        val allCities = TeamsAndCities(
            teams = emptyList(),
            cities = listOf(City(1, "Новосибирск"), City(2, "Город без стартов")),
        )
        val cache = object : InMemoryCache<TeamsAndCities>() {}
        cache.set(value = allCities)
        var requests = 0
        var status = HttpStatusCode.OK
        val client = HttpClient(MockEngine { request ->
            requests++
            assertEquals(HttpMethod.Get, request.method)
            assertEquals("/city", request.url.encodedPath)
            assertEquals("true", request.url.parameters["withStarts"])
            respond(
                content = """{"count":1,"rows":[{"id":1,"name":"Новосибирск","createdAt":null,"updatedAt":null}]}""",
                status = status,
            )
        }) { expectSuccess = true }
        val network = SportSauceNetworkTeamsCityApi(object : HttpClientProvider {
            override fun provide(loggerEnabled: Boolean) = client
            override fun json() = Json.Default
        })
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val repository = TeamsCityRepositoryBase(network, cache, ExecuteWithCacheBase())
            assertEquals(listOf(City(1, "Новосибирск")), repository.city(withStarts = true).getOrThrow())

            status = HttpStatusCode.ServiceUnavailable
            assertTrue(repository.city(withStarts = true).isFailure)
            assertEquals(2, requests)
            assertEquals(allCities, cache.get())
        } finally {
            client.close()
            Dispatchers.resetMain()
        }
    }
}
