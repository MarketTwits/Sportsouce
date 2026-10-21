package com.markettwits.sportsouce.start.presentation.album.data

import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.core.time.TimePattern
import com.markettwits.sportsouce.start.cloud.api.start.SportSauceStartApi
import com.markettwits.sportsouce.start.presentation.album.domain.*

internal class StartAlbumRepositoryBase(private val api: SportSauceStartApi) : StartAlbumRepository {
    override suspend fun album(albumId: Int): StartAlbumDetails {
        val album = api.album(albumId)
        return StartAlbumDetails(album.name, album.start.shortName,
            runCatching { BaseTimeMapper().mapTime(TimePattern.FullWithEmptySpace, album.start.startDate) }.getOrDefault(""),
            album.photos.size, album.photos.flatMap { it.tags }.map { it.name.trim() }.filter { it.isNotEmpty() }.distinct())
    }
    override suspend fun photos(albumId: Int, offset: Int, limit: Int, tags: List<String>): AlbumPhotosPage {
        val page = api.albumPhotos(albumId, offset, limit, tags)
        return AlbumPhotosPage(page.rows.map { AlbumPhoto(it.id, it.file.fullPath) }, page.count)
    }
}
