package com.markettwits.cloud_shop.model.product

import com.markettwits.cloud_shop.model.common.Category
import com.markettwits.cloud_shop.model.common.Code
import com.markettwits.cloud_shop.model.common.Image
import com.markettwits.cloud_shop.model.common.ProductOption
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val assortmentId: String,
    val brand: String?,
    val categories: List<Category>,
    val code: String,
    val codeList: List<Code>? = null,
    val createdAt: String,
    val description: String,
    val discountPrice: Int?,
    val purchasePrice : Double? = null,
    val id: String,
    val images: List<Image>? = null,
    val isHidden: Boolean,
    val model: String?,
    val name: String,
    val options: List<ProductOption>,
    val price: Int,
    val quantity: Int,
    val shopDisplayName: String?,
    val updatedAt: String,
)