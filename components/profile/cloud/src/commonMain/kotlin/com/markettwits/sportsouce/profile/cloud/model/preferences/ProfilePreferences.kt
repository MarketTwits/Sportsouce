package com.markettwits.sportsouce.profile.cloud.model.preferences

import kotlinx.serialization.Serializable

@Serializable
data class ProfileSizePreferences(
    val clothingSize: String = "",
    val footLengthMm: String = "",
    val shoeSizeUs: String = "",
    val shoeSizeRu: String = "",
    val shoeSizeJp: String = "",
)

// No defaults: empty strings must be serialized too, to clear previously saved values.
@Serializable
data class UpdateProfilePreferencesRequest(
    val clothingSize: String,
    val footLengthMm: String,
    val shoeSizeUs: String,
    val shoeSizeRu: String,
    val shoeSizeJp: String,
)

@Serializable
data class ProfileCompleteness(
    val percent: Int = 0,
    val verified: Boolean = false,
    val nextSteps: List<String> = emptyList(),
    val fields: List<ProfileCompletenessField> = emptyList(),
    val preferences: ProfileSizePreferences? = null,
)

@Serializable
data class ProfileCompletenessField(val key: String, val label: String, val filled: Boolean)
