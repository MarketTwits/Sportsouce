package com.markettwits.sportsouce.review.review.data

import com.markettwits.cahce.Cache
import com.markettwits.sportsouce.review.review.domain.ReviewPhotoAlbum
import com.markettwits.sportsouce.review.review.domain.ReviewPhotoAlbumsRepository
import com.markettwits.sportsouce.starts.albums.domain.StartsAlbumsRepository
import kotlinx.coroutines.flow.flow

internal class ReviewPhotoAlbumsRepositoryBase(
    private val repository: StartsAlbumsRepository,
    private val cache: Cache<List<ReviewPhotoAlbum>>,
) : ReviewPhotoAlbumsRepository {
    override fun albums() = flow {
        cache.get()?.let { emit(it) }
        val albums = repository.albums(offset = 0, limit = 12, filterText = "", year = null).rows
            .flatMap { start ->
                start.albums.filter { it.photosCount > 0 }.map { album ->
                    ReviewPhotoAlbum(album.id, start.name, album.name, start.date, album.photosCount, album.previewUrl)
                }
            }.distinctBy { it.id }.take(10)
        cache.set(value = albums)
        emit(albums)
    }
}
