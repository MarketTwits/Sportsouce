package com.markettwits.sportsouce.starts.favorites.data

import com.markettwits.core.log.LogTagProvider
import com.markettwits.core.log.errorLog
import com.markettwits.core.log.infoLog
import com.markettwits.sportsouce.auth.service.api.AuthDataSource
import com.markettwits.sportsouce.starts.common.domain.SportSauceStartsApi
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.favorites.domain.StartFavoritesRepository
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
    private var isRefreshing = false
    private var favoritesInitialized = false

    /**
     * Loads favorites from network and updates cache
     */
    override suspend fun refresh(forceRefresh: Boolean): Result<List<StartsListItem>> {
        // Prevent multiple simultaneous refresh calls (unless force refresh)
        if (!forceRefresh && isRefreshing) {
            infoLog { "Refresh already in progress, skipping" }
            return Result.success(_favorites.value)
        }

        return runCatching {
            isRefreshing = true
            infoLog { "Refreshing favorites from network (forceRefresh=$forceRefresh)" }
            val token = authDataSource.updateToken().getOrThrow()
            val userId = authDataSource.sharedUser().getOrThrow().id
            val favoritesList = startsApi.fetchFavoriteStarts(userId, token)

            mutex.withLock {
                favoritesInitialized = true
                _favorites.value = favoritesList
            }

            infoLog { "Favorites refreshed: ${favoritesList.size} items" }
            favoritesList
        }.onFailure { error ->
            errorLog { "Failed to refresh favorites: ${error.message}" }
        }.also {
            isRefreshing = false
        }
    }

    override suspend fun add(startsListItem: StartsListItem) {
        // Optimistically update cache first
        val (previousList, wasInitialized) = mutex.withLock {
            val previousList = _favorites.value
            val wasInitialized = favoritesInitialized
            if (_favorites.value.none { it.id == startsListItem.id }) {
                favoritesInitialized = true
                _favorites.value = _favorites.value + startsListItem
                infoLog { "Optimistically added start ${startsListItem.id} to favorites" }
            }
            previousList to wasInitialized
        }

        runCatching {
            val token = authDataSource.updateToken().getOrThrow()
            val userId = authDataSource.auth().getOrThrow().id
            startsApi.addToFavorite(
                startId = startsListItem.id,
                userId = userId,
                token = token
            )
            mutex.withLock {
                favoritesInitialized = true
            }
            infoLog { "Successfully added start ${startsListItem.id} to favorites on server" }
        }.onFailure { error ->
            errorLog { "Failed to add start ${startsListItem.id} to favorites: ${error.message}" }
            mutex.withLock {
                favoritesInitialized = wasInitialized
                _favorites.value = previousList
            }
            throw error
        }
    }

    override suspend fun remove(startsListItem: StartsListItem) {
        // Store previous state for potential rollback
        val (previousList, wasInitialized) = mutex.withLock {
            val previousList = _favorites.value
            val wasInitialized = favoritesInitialized
            _favorites.value = _favorites.value.filter { it.id != startsListItem.id }
            favoritesInitialized = true
            infoLog { "Optimistically removed start ${startsListItem.id} from favorites" }
            previousList to wasInitialized
        }

        runCatching {
            val token = authDataSource.updateToken().getOrThrow()
            val userId = authDataSource.auth().getOrThrow().id
            startsApi.removeFromFavorite(
                startId = startsListItem.id,
                userId = userId,
                token = token
            )
            mutex.withLock {
                favoritesInitialized = true
            }
            infoLog { "Successfully removed start ${startsListItem.id} from favorites on server" }
        }.onFailure { error ->
            errorLog { "Failed to remove start ${startsListItem.id} from favorites: ${error.message}" }
            mutex.withLock {
                favoritesInitialized = wasInitialized
                _favorites.value = previousList
            }
            throw error
        }
    }

    override suspend fun isStartInFavorite(startId: Int): Result<Boolean> = runCatching {
        if (!favoritesInitialized) {
            refresh().getOrThrow()
        }
        // Cache is initialized at this point, so check local state
        _favorites.value.any { it.id == startId }
    }
}
