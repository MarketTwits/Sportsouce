package com.markettwits.sportsouce.starts.cloud.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkOrganizerRemoteItem(
    val is_main: Boolean? = null,
    val name: String? = null,
    val phone: String? = null,
    val photo: NetworkOrganizerPhotoRemoteItem? = null,
)
