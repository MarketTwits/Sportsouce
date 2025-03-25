package com.markettwits.sportsouce.start.data.start.mapper.albums

import com.markettwits.sportsouce.start.cloud.model.start.fields.album.StartAlbum
import com.markettwits.sportsouce.start.domain.StartItem

internal interface StartAlbumsToUiMapper {
    fun map(startAlbum: List<StartAlbum>, sorted: Boolean = true): List<StartItem.Album>
}