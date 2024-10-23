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

    val size = products.sizeList?.map {
        ShopExtraOptions.OptionValue(
            id = it.product.id,
            optionValue = it.size,
            isProductsValue = false
        )
    }?.toMutableList()
    val colorColorTaste = products.colorTasteList?.map {
        ShopExtraOptions.OptionValue(
            id = it.product.id,
            optionValue = it.colorTaste,
            isProductsValue = false
        )
    }?.toMutableList() ?: emptyList<ShopExtraOptions.OptionValue>().toMutableList()
    val sex = products.genderList?.map {
        ShopExtraOptions.OptionValue(
            id = it.product.id,
            optionValue = it.gender,
            isProductsValue = false
        )
    }?.toMutableList()
    val currentSize = products.sizeValue?.name?.let {
        ShopExtraOptions.OptionValue(
            id = products.product.id,
            optionValue = it,
            isProductsValue = true
        )
    }
    val currentColorTaste = products.productColorTasteValue?.let {
        ShopExtraOptions.OptionValue(
            id = products.product.id,
            optionValue = it,
            isProductsValue = true
        )
    }

    val currentSex = products.productGenderValue?.let {
        ShopExtraOptions.OptionValue(
            id = products.product.id,
            optionValue = it,
            isProductsValue = true
        )
    }

    val optionsList = mutableListOf<ShopExtraOptions>()

    if (currentSize != null) {
        size?.add(currentSize)
        size?.reverse()
        optionsList.add(
            ShopExtraOptions(
                title = "Размер",
                items = size ?: emptyList()
            )
        )
    }

    if (currentColorTaste != null) {
        colorColorTaste.add(currentColorTaste)
        colorColorTaste.reverse()
        optionsList.add(
            ShopExtraOptions(
                title = "Вкус/Цвет",
                items = colorColorTaste
            )
        )
    }

    if (currentSex != null) {
        sex?.add(currentSex)
        sex?.reverse()
        optionsList.add(
            ShopExtraOptions(
                title = "Пол",
                items = sex ?: emptyList()
            )
        )
    }

    return optionsList
}

