package com.markettwits.cloud.model.news

import kotlinx.serialization.Serializable

@Serializable
data class HashtagRemote(
    val NewsToHashtag: NewsToHashtag,
    val id: Int,
    val name: String
)