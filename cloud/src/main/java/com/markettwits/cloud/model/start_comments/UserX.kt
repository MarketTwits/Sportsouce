package com.markettwits.cloud.model.start_comments

import com.markettwits.sportsourcedemo.all.PosterLinkFile
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val photo: PosterLinkFile?,
    val surname: String
)