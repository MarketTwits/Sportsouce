package com.markettwits.cloud.model.start_user

import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.sportsourcedemo.all.PosterLinkFile
import kotlinx.serialization.Serializable

@Serializable
data class RemoteStartUser(
    val coordinates: String,
    val id: Int,
    val name: String,
    val isOpen : Boolean,
    val posterLinkFile: PosterLinkFile?,
    val start_date : String,
    val select_kinds_sport: String,
    val start_status: StartStatus,
    val payment_disabled : Boolean?
)