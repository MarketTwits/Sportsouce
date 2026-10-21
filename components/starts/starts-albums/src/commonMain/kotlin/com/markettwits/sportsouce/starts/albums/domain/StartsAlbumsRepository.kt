package com.markettwits.sportsouce.starts.albums.domain

interface StartsAlbumsRepository {
    suspend fun albums(offset: Int, limit: Int, filterText: String, year: Int?): StartAlbumsPage
}
