package com.markettwits.sportsouce.club.cloud.models.trainer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrainersRemoteRow(
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("description")
    val description: String,
    @SerialName("facebook")
    val facebook: String?,
    @SerialName("file_id")
    val file_id: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("inst")
    val inst: String?,
    @SerialName("kindOfSports")
    val kindOfSports: List<KindOfSport>,
    @SerialName("name")
    val name: String,
    @SerialName("photo")
    val photo: com.markettwits.sportsouce.club.cloud.models.common.File,
    @SerialName("surname")
    val surname: String,
    @SerialName("telegram")
    val telegram: String?,
    @SerialName("twitter")
    val twitter: String?,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("vk")
    val vk: String?
)