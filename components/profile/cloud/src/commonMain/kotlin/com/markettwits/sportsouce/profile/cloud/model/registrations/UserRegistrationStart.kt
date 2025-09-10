package com.markettwits.sportsouce.profile.cloud.model.registrations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRegistrationStart(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("posterLinkFile")
    val posterLinkFile: PosterLinkFile? = null,
    @SerialName("start_date")
    val startDate: String? = null,
    @DeprecatedField
    val start_status: CloudStartStatus? = null,
    @DeprecatedField
    val coordinates: String? = null,
)