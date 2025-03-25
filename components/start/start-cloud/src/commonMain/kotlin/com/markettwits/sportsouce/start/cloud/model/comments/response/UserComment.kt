package com.markettwits.sportsouce.start.cloud.model.comments.response

import com.markettwits.sportsouce.start.cloud.model.start.fields.PosterLinkFile
import kotlinx.serialization.Serializable

@Serializable
data class UserComment(
    val id: Int,
    val name: String,
    val photo: PosterLinkFile? = null,
    val surname: String
)