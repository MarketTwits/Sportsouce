package com.markettwits.cloud_shop.model.product

import kotlinx.serialization.Serializable

@Serializable
data class ProductRemote(
    val colorTasteList: List<ColorTaste>?,
    val genderList: List<Gender>,
    val product: Product,
    val productColorTasteValue: String? = null,
    val productGenderValue: String,
    val sizeDescriptionList: List<SizeDescription>,
    val sizeList: List<Size>,
    val sizeValue: SizeValue? = null
)