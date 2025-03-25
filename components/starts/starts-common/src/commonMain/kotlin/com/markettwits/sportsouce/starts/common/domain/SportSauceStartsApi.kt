package com.markettwits.sportsouce.starts.common.domain

interface SportSauceStartsApi {
    suspend fun startWithFilter(request : Map<String, String>) : List<StartsListItem>

    suspend fun fetchActualStarts(): List<StartsListItem>

    suspend fun fetchPasteStarts(): List<StartsListItem>

    suspend fun fetchPreview(): List<StartsListItem>

    suspend fun fetchStartMain(): List<StartsListItem>
}