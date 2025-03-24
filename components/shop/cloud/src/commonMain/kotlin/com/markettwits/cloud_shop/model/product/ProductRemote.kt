package com.markettwits.cloud_shop.model.product

import kotlinx.serialization.Serializable

@Serializable
data class ProductRemote(
    val product: Product,
    val firmnessList: List<Firmness>? = null,
    val firmnessValue: Value? = null,
    val colorTasteList: List<ColorTaste>? = null,
    val genderList: List<Gender>? = null,
    val productColorTasteValue: String? = null,
    val productGenderValue: String? = null,
    val sizeList: List<Size>? = null,
    val sizeValue: SizeValue? = null,
)