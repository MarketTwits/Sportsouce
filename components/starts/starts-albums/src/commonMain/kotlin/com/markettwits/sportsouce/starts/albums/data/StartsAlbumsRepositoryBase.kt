package com.markettwits.sportsouce.starts.albums.data

import com.markettwits.core.time.BaseTimeMapper
import com.markettwits.core.time.TimePattern
import com.markettwits.sportsouce.starts.albums.domain.*
import com.markettwits.sportsouce.starts.cloud.SportSauceNetworkStartsApi

internal class StartsAlbumsRepositoryBase(private val api: SportSauceNetworkStartsApi) : StartsAlbumsRepository {
    override suspend fun albums(offset: Int, limit: Int, filterText: String, year: Int?): StartAlbumsPage {
        val result = api.photoArchive(offset, limit, filterText, year)
        return StartAlbumsPage(result.rows.map { start ->
            StartAlbumsItem(start.id, start.shortName?.takeIf { it.isNotBlank() } ?: start.name,
                runCatching { BaseTimeMapper().mapTime(TimePattern.FullWithEmptySpace, start.startDate) }.getOrDefault(""),
                start.albums.filter { it.photosCount > 0 }.map { album ->
                    StartAlbumsItem.Album(album.id, album.name, album.photosCount, album.previewPhotoPath.orEmpty())
                })
        }, result.count)
    }
}
