package com.markettwits.sportsouce.start.data.start.mapper.albums

import com.markettwits.sportsouce.start.cloud.model.start.fields.album.StartAlbum
import com.markettwits.sportsouce.start.domain.StartItem

internal class StartAlbumsToUiMapperBase : StartAlbumsToUiMapper {

    override fun map(startAlbum: List<StartAlbum>, sorted: Boolean): List<StartItem.Album> {
        val albums = startAlbum
            .filter { it.photos.isNotEmpty() }
            .map { album ->
                StartItem.Album(
                    id = album.id,
                    photos = album.photos.map { photo ->
                        StartItem.Album.Photo(
                            id = photo.id,
                            photoId = photo.fileId,
                            imageUrl = photo.file.fullPath,
                            tags = photo.tags.associate { it.id to it.name }
                        )
                    },
                    startId = album.startId,
                    name = album.name,
                )
            }
        return if (sorted) sortStartAlbum(albums) else albums
    }

    private fun sortStartAlbum(items: List<StartItem.Album>): List<StartItem.Album> {
        return items.map { album ->
            val sortedPhotos = album.photos.sortedByDescending { photo ->
                photo.tags.isNotEmpty()
            }
            album.copy(photos = sortedPhotos)
        }
    }
}