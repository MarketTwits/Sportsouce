package com.markettwits.sportsouce.profile

import com.markettwits.cahce.Cache
import com.markettwits.core_cloud.provider.HttpClientProvider
import com.markettwits.core_cloud.provider.JsonProviderBase
import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.sportsouce.auth.cloud.model.sign_in.response.User
import com.markettwits.sportsouce.profile.authorized.authorized.data.AuthorizedProfileRepositoryBase
import com.markettwits.sportsouce.profile.authorized.authorized.data.mapper.AuthorizedProfileMapper
import com.markettwits.sportsouce.profile.authorized.authorized.domain.UserProfile
import com.markettwits.sportsouce.profile.cloud.SportSauceNetworkProfileApi
import com.markettwits.sportsouce.profile.cloud.model.members.ProfileMembers
import com.markettwits.sportsouce.profile.cloud.model.registrations.UserRegistration
import com.markettwits.sportsouce.profile.registrations.data.StartOrderRegistrationRepositoryBase
import com.markettwits.sportsouce.profile.cloud.registrations.CachedUserRegistrations
import com.markettwits.sportsouce.profile.cloud.registrations.UserRegistrationsDataSource
import com.markettwits.sportsouce.profile.cloud.registrations.UserStartsRegistrationsCache
import com.markettwits.sportsouce.profile.registrations.data.mapper.UserRegistrationsMapperBase
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileRefreshRepositoryTest {
    @Test fun failedRegistrationsRequestNeverEmitsOrCachesAnEmptyProfile() = runTest {
        for (body in listOf("server unavailable", "{invalid JSON}")) {
            Fixture().use { f ->
                f.registrationsResponse = body
                f.registrationsStatus = if (body.startsWith("server")) HttpStatusCode.ServiceUnavailable else HttpStatusCode.OK
                val emitted = mutableListOf<UserProfile>()
                assertFails { f.repo.profile().toList(emitted) }
                assertEquals(listOf(profileSnapshot()), emitted)
                assertEquals(profileSnapshot(), f.cache.value)
                assertEquals(0, f.cache.writes)
                assertEquals(1, f.auth.authCalls)
                assertEquals(1, f.registrationCalls)
            }
        }
    }

    @Test fun failedMembersRequestAndCancellationAlsoPreserveLastCompleteSnapshot() = runTest {
        Fixture().use { f ->
            f.membersStatus = HttpStatusCode.ServiceUnavailable
            assertFails { f.repo.profile(true).toList() }
            assertEquals(profileSnapshot(), f.cache.value)
            assertEquals(0, f.cache.writes)
        }
        Fixture().use { f ->
            val gate = CompletableDeferred<Unit>()
            f.onRegistrations = { gate.await() }
            val job = launch { f.repo.profile().collect() }; runCurrent()
            job.cancelAndJoin()
            assertEquals(profileSnapshot(), f.cache.value)
            assertEquals(0, f.cache.writes)
        }
    }

    @Test fun cachedProfileAppearsBeforeNetworkAndARealEmptyResponseCanClearRegistrations() = runTest {
        Fixture().use { f ->
            val gate = CompletableDeferred<Unit>()
            f.auth.onAuth = { gate.await() }
            val values = mutableListOf<UserProfile>()
            val job = launch { f.repo.profile().toList(values) }; runCurrent()
            assertEquals(listOf(profileSnapshot()), values)
            gate.complete(Unit); job.join()
            assertEquals(listOf(10, 11), values.map { it.activity.userRegistry.single().id })
            assertEquals(values.last(), f.cache.value)
            f.registrationsResponse = """{"count":0,"rows":[]}"""
            val next = f.repo.profile().toList()
            assertEquals(listOf(11), next.first().activity.userRegistry.map { it.id })
            assertTrue(next.last().activity.userRegistry.isEmpty(), "A successful empty response is authoritative")
            assertEquals(next.last(), f.cache.value)
            assertEquals(2, f.auth.authCalls, "Exactly one auth refresh per profile load")
        }
    }

    @Test fun firstLoadFailureAndAnotherAccountsCacheMustNotShowFakeRegistrations() = runTest {
        Fixture().use { f ->
            f.cache.value = null
            f.registrationsStatus = HttpStatusCode.ServiceUnavailable
            val values = mutableListOf<UserProfile>()
            assertFails { f.repo.profile().toList(values) }
            assertTrue(values.isEmpty()); assertNull(f.cache.value)
        }
        Fixture().use { f ->
            f.auth.current = f.auth.current.copy(id = 2)
            f.registrationsStatus = HttpStatusCode.ServiceUnavailable
            val values = mutableListOf<UserProfile>()
            assertFails { f.repo.profile().toList(values) }
            assertTrue(values.isEmpty(), "Never display the previous account's cached registrations")
        }
    }

    @Test fun profileAndOrdersUseTheSameAccountSnapshotAndKeepItOnFailedRefresh() = runTest {
        Fixture().use { f ->
            f.repo.profile().toList()
            val newlyRegistered = """{"count":1,"rows":[{"id":12,"combo_id":null}]}"""
            f.registrationsResponse = newlyRegistered
            assertEquals(listOf(listOf(11), listOf(12)), f.orders.registrations(forced = false).toList()
                .map { snapshot -> snapshot.map { it.id } })

            f.registrationsStatus = HttpStatusCode.ServiceUnavailable
            val cachedProfiles = mutableListOf<UserProfile>()
            assertFails { f.repo.profile().toList(cachedProfiles) }
            assertEquals(listOf(12), cachedProfiles.single().activity.userRegistry.map { it.id })
            assertEquals(listOf(12), f.orders.registrations(forced = false).toList().single().map { it.id })
            assertFails { f.orders.registrations(forced = true, allowCachedOnError = false).toList() }

            f.auth.current = f.auth.current.copy(id = 2)
            assertFails { f.orders.registrations(forced = false).toList() }
        }
    }

    @Test fun profileUsesTheNewerRegistrationsWhenItsMembersRequestFinishesLater() = runTest {
        Fixture().use { f ->
            val finishMembers = CompletableDeferred<Unit>()
            f.onMembers = { finishMembers.await() }
            val profile = async { f.repo.profile(forced = true).single() }
            try {
                withContext(Dispatchers.Default) {
                    withTimeout(5_000) { f.orderCache.writesFlow.first { it == 1 } }
                }
                f.registrationsResponse = """{"count":1,"rows":[{"id":12,"combo_id":null}]}"""
                assertEquals(12, f.orders.registrations(forced = true).single().single().id)
            } finally {
                finishMembers.complete(Unit)
            }
            assertEquals(12, profile.await().activity.userRegistry.single().id)
        }
    }

    private class MemoryCache<T>(var value: T? = null) : Cache<T> {
        val writesFlow = MutableStateFlow(0)
        val writes get() = writesFlow.value
        override suspend fun get(key: Any) = value
        override suspend fun set(key: Any, value: T) { this.value = value; writesFlow.value++ }
        override suspend fun clear() { value = null }
        override suspend fun getList(key: Any) = emptyList<T>()
    }

    private class Fixture : AutoCloseable {
        val auth = ProfileAuth()
        val cache = MemoryCache(profileSnapshot())
        var registrationsStatus = HttpStatusCode.OK
        var membersStatus = HttpStatusCode.OK
        var registrationsResponse = """{"count":1,"rows":[{"id":11,"combo_id":null}]}"""
        var registrationCalls = 0
        var onRegistrations: suspend () -> Unit = {}
        var onMembers: suspend () -> Unit = {}
        val client = HttpClient(MockEngine { request ->
            if (request.url.encodedPath == "/member-template") {
                onMembers()
                respond("""{"count":0,"rows":[]}""", membersStatus, headersOf(HttpHeaders.ContentType, "application/json"))
            } else {
                assertTrue(request.url.encodedPath.startsWith("/user/startsByUserId/"))
                registrationCalls++; onRegistrations()
                respond(registrationsResponse, registrationsStatus, headersOf(HttpHeaders.ContentType, "application/json"))
            }
        }) { expectSuccess = true; defaultRequest { url("https://api.example/") } }
        private val remote = SportSauceNetworkProfileApi(object : HttpClientProvider {
            override fun provide(loggerEnabled: Boolean) = client
            override fun json() = JsonProviderBase().provide()
        })
        val orderCache = MemoryCache<CachedUserRegistrations>()
        private val registrations = UserRegistrationsDataSource(remote, UserStartsRegistrationsCache(orderCache) { null })
        val orders = StartOrderRegistrationRepositoryBase(remote, auth, UserRegistrationsMapperBase(BaseTimeMapper()), registrations)
        val repo = AuthorizedProfileRepositoryBase(auth, remote, cache, object : AuthorizedProfileMapper {
            override fun mapRegistrations(registrations: List<UserRegistration>) =
                profileSnapshot(registrationIds = registrations.map { it.id!! }).activity.userRegistry

            override fun map(user: User, userRegistries: List<UserRegistration>, userMembers: List<ProfileMembers.ProfileMember>) =
                profileSnapshot(user.id, userRegistries.map { it.id!! })
        }, registrations)
        override fun close() { client.close() }
    }
}
