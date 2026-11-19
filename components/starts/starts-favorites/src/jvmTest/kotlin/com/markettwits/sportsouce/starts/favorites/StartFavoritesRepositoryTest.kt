package com.markettwits.sportsouce.starts.favorites

import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.starts.common.domain.SportSauceStartsApi
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.favorites.data.StartFavoritesRepositoryBase
import java.lang.reflect.Proxy
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.test.*

class StartFavoritesRepositoryTest {
    @Test
    fun membershipWaitsForAnOngoingRefreshInsteadOfReturningAnEmptyCache() = runBlocking {
        val allowResponse = CompletableDeferred<Unit>()
        val api = object : SportSauceStartsApi by unused<SportSauceStartsApi>() {
            override suspend fun fetchFavoriteStarts(userId: Int, token: String): List<StartsListItem> {
                allowResponse.await()
                return listOf(start)
            }
        }
        val repository = StartFavoritesRepositoryBase(auth, api)
        val refresh = async(start = CoroutineStart.UNDISPATCHED) { repository.refresh() }
        val favorite = async(start = CoroutineStart.UNDISPATCHED) { repository.isStartInFavorite(start.id) }
        try {
            assertFalse(favorite.isCompleted)
        } finally {
            allowResponse.complete(Unit)
        }
        assertEquals(listOf(start), refresh.await().getOrThrow())
        assertTrue(favorite.await().getOrThrow())
    }

    @Test
    fun duplicateAddUpdatesTheCacheButAnActualFailureIsReported() = runBlocking {
        var serverFavorites = emptyList<StartsListItem>()
        val backendError = IllegalStateException("Add rejected")
        val api = object : SportSauceStartsApi by unused<SportSauceStartsApi>() {
            override suspend fun fetchFavoriteStarts(userId: Int, token: String) = serverFavorites
            override suspend fun addToFavorite(userId: Int, token: String, startId: Int) = throw backendError
        }
        val repository = StartFavoritesRepositoryBase(auth, api)
        assertFalse(repository.isStartInFavorite(start.id).getOrThrow())
        serverFavorites = listOf(start)
        repository.add(start)
        assertEquals(listOf(start), repository.favorites.value)

        serverFavorites = emptyList()
        val error = assertFailsWith<IllegalStateException> { repository.add(start) }
        assertSame(backendError, error)
        assertEquals(emptyList(), repository.favorites.value)
    }

    private val auth = object : AuthDataSource by unused<AuthDataSource>() {
        override suspend fun updateToken() = Result.success("token")
        override suspend fun userID() = Result.success(42)
    }

    private val start = StartsListItem(315, "Трейл", "", "25 октября 2026", StartsListItem.StatusCode(3, ""),
        "", "trail", "", false, "", kindOfSports = emptyList(), views = 0)

    private inline fun <reified T> unused(): T =
        Proxy.newProxyInstance(T::class.java.classLoader, arrayOf(T::class.java)) { _, method, _ ->
            error("Unexpected ${method.name}")
        } as T
}
