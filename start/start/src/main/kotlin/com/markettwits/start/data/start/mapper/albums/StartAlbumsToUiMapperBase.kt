package com.markettwits.start.data.start.mapper.albums

import com.markettwits.cloud.model.start_album.StartAlbumRemote
import com.markettwits.start.domain.StartItem

internal class StartAlbumsToUiMapperBase : StartAlbumsToUiMapper {
    override fun map(startAlbum: StartAlbumRemote, sorted: Boolean): List<StartItem.Album> {
        val photos = startAlbum.rows.flatMap { row ->
            row.photos.map { photoRemote ->
                StartItem.Album.Photo(
                    id = photoRemote.id,
                    photoId = photoRemote.file_id,
                    imageUrl = photoRemote.file.fullPath,
                    tags = photoRemote.tags.associate { it.id to it.name }
                )
            }
        }
        val albums = startAlbum.rows.map {
            StartItem.Album(
                id = it.id,
                startId = it.start_id,
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