package com.markettwits.start_cloud.model.start.fields.album

import com.markettwits.start_cloud.model.start.fields.File
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
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
        @kotlinx.serialization.Serializable
        data class Photo(
            val album_id: Int,
            val createdAt: String,
            val file: File,
            val file_id: Int,
            val id: Int,
            val tags: List<Tag>,
            val updatedAt: String
        ) {
            @kotlinx.serialization.Serializable
            data class Tag(
                @SerialName("PhotosToTag")
                val photosToTag: PhotosToTag,
                val createdAt: String,
                val id: Int,
                val name: String,
                val updatedAt: String
            ) {
                @kotlinx.serialization.Serializable
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