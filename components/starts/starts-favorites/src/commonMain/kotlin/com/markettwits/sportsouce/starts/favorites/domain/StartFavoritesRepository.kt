package com.markettwits.sportsouce.starts.favorites.domain

import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import kotlinx.coroutines.flow.StateFlow

interface StartFavoritesRepository {

    /**
     * Observable state of favorite starts cached in memory
     */
    val favorites: StateFlow<List<StartsListItem>>

    /**
     * Refreshes favorites from network and updates cache
     */
    suspend fun refresh(): Result<List<StartsListItem>>

    /**
     * Adds start to favorites (optimistic update with rollback on failure)
     */
    suspend fun add(startsListItem: StartsListItem)

    /**
     * Removes start from favorites (optimistic update with rollback on failure)
     */
    suspend fun remove(startsListItem: StartsListItem)

    /**
     * Checks the current server membership and refreshes the local list.
     */
    suspend fun isStartInFavorite(startId: Int): Result<Boolean>
}
