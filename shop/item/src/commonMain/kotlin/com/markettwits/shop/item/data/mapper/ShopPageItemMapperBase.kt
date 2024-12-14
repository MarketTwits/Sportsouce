package com.markettwits.shop.item.data.mapper

import com.markettwits.cloud_shop.model.product.ProductRemote
import com.markettwits.shop.domain.mapper.ShopProductsMapper
import com.markettwits.shop.domain.model.ShopItem
import com.markettwits.shop.item.domain.models.ShopExtraOptions
import java.text.NumberFormat
import java.util.Locale

class ShopPageItemMapperBase(
    private val shopProductsMapper: ShopProductsMapper
) : ShopPageItemMapper {

    override fun map(products: ProductRemote): Pair<ShopItem,List<ShopExtraOptions>> =
        Pair(
            first = shopProductsMapper.map(products.product),
            second = calculateExtras(products),
        )
}

private fun calculateExtras(products: ProductRemote): List<ShopExtraOptions> {
    val categories = listOf(
        Category(
            title = "Жесткость",
            list = products.firmnessList?.map {
                ShopExtraOptions.OptionValue(
                    id = it.product.id,
                    optionValue = it.firmness,
                    isProductsValue = false
                )
            },
            currentValue = products.firmnessValue?.let {
                ShopExtraOptions.OptionValue(
                    id = products.product.id,
                    optionValue = it.name,
                    isProductsValue = true
                )
            }
        ),
        Category(
            title = "Размер",
            list = products.sizeList?.map {
                ShopExtraOptions.OptionValue(
                    id = it.product.id,
                    optionValue = it.size,
                    isProductsValue = false
                )
            },
            currentValue = products.sizeValue?.name?.let {
                ShopExtraOptions.OptionValue(
                    id = products.product.id,
                    optionValue = it,
                    isProductsValue = true
                )
            }
        ),
        Category(
            title = "Вкус/Цвет",
            list = products.colorTasteList?.map {
                ShopExtraOptions.OptionValue(
                    id = it.product.id,
                    optionValue = it.colorTaste,
                    isProductsValue = false
                )
            },
            currentValue = products.productColorTasteValue?.let {
                ShopExtraOptions.OptionValue(
                    id = products.product.id,
                    optionValue = it,
                    isProductsValue = true
                )
            }
        ),
        Category(
            title = "Пол",
            list = products.genderList?.map {
                ShopExtraOptions.OptionValue(
                    id = it.product.id,
                    optionValue = it.gender,
                    isProductsValue = false
                )
            },
            currentValue = products.productGenderValue?.let {
                ShopExtraOptions.OptionValue(
                    id = products.product.id,
                    optionValue = it,
                    isProductsValue = true
                )
            }
        )
    )

    return categories.mapNotNull { category ->
        category.toShopExtraOptions()
    }
}


private data class Category(
    val title: String,
    val list: List<ShopExtraOptions.OptionValue>?,
    val currentValue: ShopExtraOptions.OptionValue?
) {
    fun toShopExtraOptions(): ShopExtraOptions? {
        val allItems = list?.toMutableList() ?: mutableListOf()
        currentValue?.let { current ->
            if (allItems.none { it.optionValue == current.optionValue }) {
                allItems.add(0, current) // Добавляем в начало
            }
        }
        return if (allItems.isNotEmpty()) {
            ShopExtraOptions(title = title, items = allItems)
        } else {
            null
        }
    }
}

