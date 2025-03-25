package com.markettwits.sportsouce.profile.cloud.model.registrations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRegistrationStart(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("posterLinkFile")
    val posterLinkFile: PosterLinkFile?,
    @SerialName("start_date")
    val startDate: String,
    @DeprecatedField
    val start_status: CloudStartStatus? = null,
    @DeprecatedField
    val coordinates: String? = null,
)