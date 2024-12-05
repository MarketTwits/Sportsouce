package com.markettwits.club.cloud.models.trainer

import kotlinx.serialization.Serializable

@Serializable
data class TrainersRemoteRow(
    val createdAt: String,
    val description: String,
    val facebook: String?,
    val file_id: Int,
    val id: Int,
    val inst: String?,
    val kindOfSports: List<KindOfSport>,
    val name: String,
    val photo: com.markettwits.club.cloud.models.common.File,
    val surname: String,
    val telegram: String?,
    val twitter: String?,
    val updatedAt: String,
    val vk: String?
)