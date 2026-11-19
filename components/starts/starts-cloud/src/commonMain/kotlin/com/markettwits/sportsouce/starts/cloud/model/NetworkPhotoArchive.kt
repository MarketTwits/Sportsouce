package com.markettwits.sportsouce.starts.cloud.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPhotoArchive(val rows: List<Start> = emptyList(), val count: Int = 0) {
    @Serializable
    data class Start(
        val id: Int,
        val name: String,
        @SerialName("short_name") val shortName: String? = null,
        @SerialName("start_date") val startDate: String,
        @SerialName("albums_count") val albumsCount: Int = 0,
        @SerialName("photos_count") val photosCount: Int = 0,
        val albums: List<Album> = emptyList(),
    )

    @Serializable
    data class Album(
        val id: Int,
        val name: String,
        val isBeforeStart: Boolean? = null,
        @SerialName("photos_count") val photosCount: Int = 0,
        @SerialName("preview_photo_path") val previewPhotoPath: String? = null,
    )
}
