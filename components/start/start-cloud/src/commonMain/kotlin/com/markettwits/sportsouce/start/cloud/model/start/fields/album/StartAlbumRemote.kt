package com.markettwits.sportsouce.start.cloud.model.start.fields.album

@kotlinx.serialization.Serializable
internal data class StartAlbumRemote(
    val count: Int,
    val rows: List<StartAlbum>
)