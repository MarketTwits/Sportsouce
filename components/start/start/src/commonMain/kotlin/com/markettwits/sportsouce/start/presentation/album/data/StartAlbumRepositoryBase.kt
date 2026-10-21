package com.markettwits.sportsouce.start.presentation.album.data

import com.markettwits.sportsouce.start.cloud.api.start.SportSauceStartApi
import com.markettwits.sportsouce.start.presentation.album.domain.StartAlbumRepository

internal class StartAlbumRepositoryBase(private val api: SportSauceStartApi) : StartAlbumRepository {
    override suspend fun images(albumId: Int) = api.album(albumId).photos.map { it.file.fullPath }.filter { it.isNotBlank() }
}
