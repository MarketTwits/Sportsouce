package com.markettwits.sportsouce.shop.cloud.model.product

import com.markettwits.sportsouce.shop.cloud.model.common.Category
import com.markettwits.sportsouce.shop.cloud.model.common.Code
import com.markettwits.sportsouce.shop.cloud.model.common.Image
import com.markettwits.sportsouce.shop.cloud.model.common.ProductOption
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val assortmentId: String?,
    val brand: String?,
    val categories: List<Category>,
    val code: String,
    val codeList: List<Code>? = null,
    val description: String,
    val discountPrice: Int?,
    val id: String,
    val images: List<Image>? = null,
    val isHidden: Boolean,
    val model: String?,
    val name: String,
    val options: List<ProductOption>,
    val price: Int,
    val quantity: Int,
    val shopDisplayName: String?,
)