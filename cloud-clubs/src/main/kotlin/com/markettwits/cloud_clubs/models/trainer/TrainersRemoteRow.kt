package com.markettwits.cloud_clubs.models.trainer

import com.markettwits.cloud_clubs.models.common.File
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
    val photo: File,
    val surname: String,
    val telegram: String?,
    val twitter: String?,
    val updatedAt: String,
    val vk: String?
)