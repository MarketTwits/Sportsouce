package com.markettwits.start.data.start.mapper.albums

import com.markettwits.cloud.model.start_album.StartAlbumRemote
import com.markettwits.start.domain.StartItem
import com.markettwits.start_cloud.model.start.fields.album.StartAlbum

internal interface StartAlbumsToUiMapper {
    fun map(startAlbum: List<StartAlbum>, sorted: Boolean = true): List<StartItem.Album>
}