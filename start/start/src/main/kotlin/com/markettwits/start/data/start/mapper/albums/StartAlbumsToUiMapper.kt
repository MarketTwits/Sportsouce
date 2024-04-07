package com.markettwits.start.data.start.mapper.albums

import com.markettwits.cloud.model.start_album.StartAlbumRemote
import com.markettwits.start.domain.StartItem

internal interface StartAlbumsToUiMapper {
    fun map(startAlbum: StartAlbumRemote, sorted: Boolean = true): List<StartItem.Album>
}