package com.markettwits.sportsouce.edit_profile.sizes.domain

enum class ProfileSizeField { Clothing, FootLength, ShoeRu, ShoeUs, ShoeJp }

data class ProfileSizes(
    val clothingSize: String = "",
    val footLengthMm: String = "",
    val shoeSizeRu: String = "",
    val shoeSizeUs: String = "",
    val shoeSizeJp: String = "",
) {
    fun value(field: ProfileSizeField): String = when (field) {
        ProfileSizeField.Clothing -> clothingSize
        ProfileSizeField.FootLength -> footLengthMm
        ProfileSizeField.ShoeRu -> shoeSizeRu
        ProfileSizeField.ShoeUs -> shoeSizeUs
        ProfileSizeField.ShoeJp -> shoeSizeJp
    }

    fun updated(field: ProfileSizeField, value: String): ProfileSizes = when (field) {
        ProfileSizeField.Clothing -> copy(clothingSize = value)
        ProfileSizeField.FootLength -> copy(footLengthMm = value)
        ProfileSizeField.ShoeRu -> copy(shoeSizeRu = value)
        ProfileSizeField.ShoeUs -> copy(shoeSizeUs = value)
        ProfileSizeField.ShoeJp -> copy(shoeSizeJp = value)
    }

    fun normalized(): ProfileSizes = ProfileSizeField.entries.fold(this) { data, field ->
        val value = value(field).trim()
        data.updated(field, if (field == ProfileSizeField.Clothing) value else value.replace(',', '.'))
    }

    fun errors(): Map<ProfileSizeField, String> = ProfileSizeField.entries.mapNotNull { field ->
        val value = value(field).trim()
        val error = when {
            value.length > 20 -> "Не более 20 символов"
            value.isEmpty() || field == ProfileSizeField.Clothing -> null
            !Regex("[0-9]+([.,][0-9]+)?").matches(value) || (value.replace(',', '.').toDoubleOrNull() ?: 0.0) <= 0.0 ->
                "Введите число больше нуля"
            else -> null
        }
        error?.let { field to it }
    }.toMap()
}
