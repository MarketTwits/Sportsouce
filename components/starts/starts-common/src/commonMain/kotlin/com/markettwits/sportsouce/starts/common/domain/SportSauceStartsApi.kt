package com.markettwits.sportsouce.starts.common.domain

interface SportSauceStartsApi {

    suspend fun startWithFilter(request : Map<String, String>) : List<StartsListItem>

    suspend fun fetchActualStarts(): List<StartsListItem>

    suspend fun fetchPasteStarts(): List<StartsListItem>

    suspend fun fetchPreviewStarts(): List<StartsListItem>

    suspend fun fetchRelatedStarts(seriesId: Int): List<StartsListItem>

    suspend fun fetchStartMain(): List<StartsListItem>

    suspend fun fetchFavoriteStarts(userId: Int, token: String): List<StartsListItem>

    suspend fun addToFavorite(userId: Int, token: String, startId: Int)

    suspend fun removeFromFavorite(userId: Int, token: String, startId: Int)
}