package com.markettwits.sportsouce.profile.cloud.model.members

import kotlinx.serialization.Serializable

@Serializable
internal data class MemberMutationResponse(val success: Boolean? = null, val message: String? = null)
