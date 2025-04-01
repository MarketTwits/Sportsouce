package com.markettwits.sportsouce.start.cloud.model.start.fields.album

import com.markettwits.sportsouce.start.cloud.model.start.fields.File
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
    data class StartAlbum(
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("id")
    val id: Int,
    @SerialName("isBeforeStart")
    val isBeforeStart: Boolean? = null,
    @SerialName("name")
    val name: String,
    @SerialName("photos")
    val photos: List<Photo>,
    @SerialName("start")
    val start: Start,
    @SerialName("start_id")
    val startId: Int,
    @SerialName("updatedAt")
    val updatedAt: String
    ) {
        @Serializable
        data class Photo(
            @SerialName("album_id") val albumId: Int,
            @SerialName("createdAt") val createdAt: String,
            @SerialName("file") val file: File,
            @SerialName("file_id") val fileId: Int,
            @SerialName("id") val id: Int,
            @SerialName("tags") val tags: List<Tag>,
            @SerialName("updatedAt") val updatedAt: String
        ) {
            @Serializable
            data class Tag(
                @SerialName("PhotosToTag") val photosToTag: PhotosToTag,
                @SerialName("createdAt") val createdAt: String,
                @SerialName("id") val id: Int,
                @SerialName("name") val name: String,
                @SerialName("updatedAt") val updatedAt: String
            ) {
                @Serializable
                data class PhotosToTag(
                    @SerialName("id") val id: Int,
                    @SerialName("photoTag_id") val phototagId: Int,
                    @SerialName("photo_id") val photoId: Int
                )
            }
        }

        @Serializable
        data class Start(
            @SerialName("id") val id: Int,
            @SerialName("short_name") val shortName: String,
            @SerialName("start_date") val startDate: String
        )
    }