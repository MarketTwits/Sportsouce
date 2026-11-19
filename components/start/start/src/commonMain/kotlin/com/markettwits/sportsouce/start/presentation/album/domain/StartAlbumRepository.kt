package com.markettwits.sportsouce.start.presentation.album.domain

interface StartAlbumRepository {
    suspend fun album(albumId: Int): StartAlbumDetails
    suspend fun photos(albumId: Int, offset: Int, limit: Int, tags: List<String>): AlbumPhotosPage
}
