package com.markettwits.sportsouce.start.cloud.model.comments.response

import com.markettwits.sportsouce.start.cloud.model.start.fields.PosterLinkFile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserComment(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("photo")
    val photo: PosterLinkFile? = null,
    @SerialName("surname")
    val surname: String
)