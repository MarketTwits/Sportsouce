package com.markettwits.sportsouce.starts.favorites.data

import com.markettwits.core.log.LogTagProvider
import com.markettwits.core.log.errorLog
import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.starts.common.domain.SportSauceStartsApi
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.favorites.domain.StartFavoritesRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class StartFavoritesRepositoryBase(
    private val authDataSource: AuthDataSource,
    private val startsApi: SportSauceStartsApi,
) : StartFavoritesRepository, LogTagProvider {

    override val tag: String = "StartFavoritesRepository"

    private val _favorites = MutableStateFlow<List<StartsListItem>>(emptyList())
    override val favorites: StateFlow<List<StartsListItem>> = _favorites.asStateFlow()

    private val mutex = Mutex()
    private var cachedUserId: Int? = null

    override suspend fun refresh(): Result<List<StartsListItem>> = runCatching {
        mutex.withLock {
            val token = authDataSource.updateToken().getOrThrow()
            val userId = authDataSource.userID().getOrThrow()
            selectUser(userId)
            fetchFavorites(userId, token)
        }
    }.onFailure { error ->
        if (error is CancellationException) throw error
        errorLog { "Failed to refresh favorites: ${error.message}" }
    }

    override suspend fun add(startsListItem: StartsListItem) = setFavorite(startsListItem, true)

    override suspend fun remove(startsListItem: StartsListItem) = setFavorite(startsListItem, false)

    override suspend fun isStartInFavorite(startId: Int): Result<Boolean> =
        refresh().map { favorites -> favorites.any { it.id == startId } }

    private suspend fun setFavorite(item: StartsListItem, favorite: Boolean): Unit = mutex.withLock {
        val token = authDataSource.updateToken().getOrThrow()
        val userId = authDataSource.userID().getOrThrow()
        selectUser(userId)
        val previous = _favorites.value
        _favorites.value = if (favorite) (previous + item).distinctBy { it.id } else previous.filterNot { it.id == item.id }

        try {
            if (favorite) startsApi.addToFavorite(userId, token, item.id)
            else startsApi.removeFromFavorite(userId, token, item.id)
        } catch (cancelled: CancellationException) {
            _favorites.value = previous
            throw cancelled
        } catch (error: Exception) {
            _favorites.value = previous
            // A rejected write can mean that the requested state is already set on the server.
            val actual = runCatching { fetchFavorites(userId, token) }.getOrElse {
                if (it is CancellationException) throw it
                throw error
            }
            if (actual.any { it.id == item.id } != favorite) throw error
        }
    }

    private fun selectUser(userId: Int) {
        if (cachedUserId != userId) {
            cachedUserId = userId
            _favorites.value = emptyList()
        }
    }

    private suspend fun fetchFavorites(userId: Int, token: String): List<StartsListItem> =
        startsApi.fetchFavoriteStarts(userId, token).also { _favorites.value = it }
}
