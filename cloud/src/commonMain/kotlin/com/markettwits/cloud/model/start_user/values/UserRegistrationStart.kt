package com.markettwits.cloud.model.start_user.values

import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.sportsourcedemo.all.PosterLinkFile
import kotlinx.serialization.Serializable

@Serializable
data class UserRegistrationStart(
    val id: Int,
    val name: String,
    val posterLinkFile: PosterLinkFile?,
    val start_date: String,
    @DeprecatedField
    val start_status: StartStatus? = null,
    @DeprecatedField
    val coordinates: String? = null,
)