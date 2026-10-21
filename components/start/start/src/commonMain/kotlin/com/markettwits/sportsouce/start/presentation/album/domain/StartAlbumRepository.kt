package com.markettwits.sportsouce.start.presentation.album.domain

interface StartAlbumRepository {
    suspend fun images(albumId: Int): List<String>
}
