package com.markettwits.profile.cloud.model.registrations

import kotlinx.serialization.Serializable

@Serializable
data class Stage(val value: String, val sex: List<String>)