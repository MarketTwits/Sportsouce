package com.markettwits.news.cloud.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkHashtagRemote(
    @SerialName("NewsToHashtag")
    val newsToHashtag: NetworkNewsToHashtag,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)