package com.markettwits.sportsouce.start.cloud.model.start.fields.album

import kotlinx.serialization.Serializable

@Serializable
data class AlbumPhotosRemote(val count: Int, val rows: List<StartAlbum.Photo>)
