package com.markettwits.cloud_shop.model.product

import kotlinx.serialization.Serializable

@Serializable
data class ProductRemote(
    val colorTasteList: List<ColorTaste>? = null,
    val genderList: List<Gender>? = null,
    val product: Product,
    val productColorTasteValue: String? = null,
    val productGenderValue: String? = null,
    val sizeDescriptionList: List<SizeDescription>? = null,
    val sizeList: List<Size>? = null,
    val sizeValue: SizeValue? = null,
)