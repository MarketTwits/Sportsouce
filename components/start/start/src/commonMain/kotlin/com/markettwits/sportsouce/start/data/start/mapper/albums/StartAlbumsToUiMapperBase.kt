package com.markettwits.sportsouce.start.data.start.mapper.albums

import com.markettwits.sportsouce.start.cloud.model.start.fields.album.StartAlbum
import com.markettwits.sportsouce.start.domain.StartItem

internal class StartAlbumsToUiMapperBase : StartAlbumsToUiMapper {

    override fun map(startAlbum: List<StartAlbum>, sorted: Boolean): List<StartItem.Album> {
        val photos = startAlbum.flatMap { row ->
            row.photos.map { photoRemote ->
                StartItem.Album.Photo(
                    id = photoRemote.id,
                    photoId = photoRemote.fileId,
                    imageUrl = photoRemote.file.fullPath,
                    tags = photoRemote.tags.associate { it.id to it.name }
                )
            }
        }
        val albums = startAlbum.map {
            StartItem.Album(
                id = it.id,
                startId = it.startId,
                photos = photos,
                name = it.name,
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