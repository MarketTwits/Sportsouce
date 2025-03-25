package com.markettwits.sportsouce.start.cloud.model.start.fields.album

import com.markettwits.sportsouce.start.cloud.model.start.fields.File
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
    data class StartAlbum(
    val createdAt: String,
    val id: Int,
    val isBeforeStart: Boolean? = null,
    val name: String,
    val photos: List<Photo>,
    val start: Start,
    val start_id: Int,
    val updatedAt: String
    ) {
        @Serializable
        data class Photo(
            val album_id: Int,
            val createdAt: String,
            val file: File,
            val file_id: Int,
            val id: Int,
            val tags: List<Tag>,
            val updatedAt: String
        ) {
            @Serializable
            data class Tag(
                @SerialName("PhotosToTag")
                val photosToTag: PhotosToTag,
                val createdAt: String,
                val id: Int,
                val name: String,
                val updatedAt: String
            ) {
                @Serializable
                data class PhotosToTag(
                    val id: Int,
                    val photoTag_id: Int,
                    val photo_id: Int
                )
            }
        }

        @Serializable
        data class Start(
            val id: Int,
            val short_name: String,
            val start_date: String
        )
    }