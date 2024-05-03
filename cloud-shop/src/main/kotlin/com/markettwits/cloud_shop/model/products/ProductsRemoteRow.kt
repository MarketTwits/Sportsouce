package com.markettwits.cloud_shop.model.products

import com.markettwits.cloud_shop.model.common.Category
import com.markettwits.cloud_shop.model.common.Code
import com.markettwits.cloud_shop.model.common.Image
import com.markettwits.cloud_shop.model.common.OptionWithInfo
import kotlinx.serialization.Serializable

@Serializable
data class ProductsRemoteRow(
    val assortmentId: String?,
    val brand: String,
    val categories: List<Category>,
    val code: String,
    val codeList: List<Code>,
    val createdAt: String,
    val description: String,
    val discountPrice: Int?,
    val id: String,
    val images: List<Image>,
    val isHidden: Boolean,
    val model: String,
    val name: String,
    val options: List<OptionWithInfo>,
    val price: Int,
    val purchasePrice: Double,
    val quantity: Int,
    val shopDisplayName: String?,
    val updatedAt: String
)