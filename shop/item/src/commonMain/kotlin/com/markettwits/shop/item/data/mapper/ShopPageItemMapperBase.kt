package com.markettwits.shop.item.data.mapper

import com.markettwits.cloud_shop.model.product.ProductRemote
import com.markettwits.shop.domain.mapper.ShopProductsMapper
import com.markettwits.shop.domain.model.ShopItem
import com.markettwits.shop.item.domain.models.ShopExtraOptions

class ShopPageItemMapperBase(
    private val shopProductsMapper: ShopProductsMapper
) : ShopPageItemMapper {

    override fun map(products: ProductRemote): Pair<ShopItem,List<ShopExtraOptions>> =
        Pair(
            first = shopProductsMapper.map(products.product),
            second = calculateExtras(products),
        )

    override fun mapWithPrevOptions(
        newValue: Pair<ShopItem, List<ShopExtraOptions>>,
        prevValue: Pair<ShopItem, List<ShopExtraOptions>>
    ) : Pair<ShopItem, List<ShopExtraOptions>> {
        val (newShopItem, newOptions) = newValue
        val (_, prevOptions) = prevValue

        val sortedOptions = newOptions.map { newCategory ->
            val prevCategory = prevOptions.find { it.title == newCategory.title }

            if (prevCategory != null) {
                val prevOrder = prevCategory.items.map { it.optionValue }
                val sortedItems = newCategory.items.sortedWith(compareBy {
                    prevOrder.indexOf(it.optionValue).takeIf { it >= 0 } ?: Int.MAX_VALUE
                })
                newCategory.copy(items = sortedItems)
            } else {
                newCategory
            }
        }

        return newShopItem to sortedOptions
    }
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
            val existingItemIndex = allItems.indexOfFirst { it.optionValue == current.optionValue }
            if (existingItemIndex != -1) {
                allItems[existingItemIndex] = allItems[existingItemIndex].copy(isProductsValue = true)
            } else {
                allItems.add(current)
            }
        }

        return if (allItems.isNotEmpty()) {
            ShopExtraOptions(title = title, items = allItems.sortedBy { it.optionValue })
        } else {
            null
        }
    }
}

