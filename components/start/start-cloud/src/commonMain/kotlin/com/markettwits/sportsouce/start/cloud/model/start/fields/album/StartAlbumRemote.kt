package com.markettwits.sportsouce.start.cloud.model.start.fields.album

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class StartAlbumRemote(
    @SerialName("count")
    val count: Int,
    @SerialName("rows")
    val rows: List<StartAlbum>
)