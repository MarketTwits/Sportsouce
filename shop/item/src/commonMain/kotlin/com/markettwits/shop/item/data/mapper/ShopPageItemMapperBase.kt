package com.markettwits.shop.item.data.mapper

import com.markettwits.cloud_shop.model.product.ProductRemote
import com.markettwits.shop.domain.mapper.ShopProductsMapper
import com.markettwits.shop.item.domain.models.ShopPageItem
import java.text.NumberFormat
import java.util.Locale

class ShopPageItemMapperBase(
    private val shopProductsMapper: ShopProductsMapper
) : ShopPageItemMapper {

    override fun map(products: ProductRemote): ShopPageItem =
        ShopPageItem(
            product = shopProductsMapper.map(products.product),
            extraOptions = calculateExtras(products),
        )
}

private fun calculateExtras(products: ProductRemote): List<ShopPageItem.ExtraOption> {

    val size = products.sizeList?.map {
        ShopPageItem.ExtraOption.OptionValue(
            id = it.product.id,
            optionValue = it.size,
            isProductsValue = false
        )
    }?.toMutableList()
    val colorColorTaste = products.colorTasteList?.map {
        ShopPageItem.ExtraOption.OptionValue(
            id = it.product.id,
            optionValue = it.colorTaste,
            isProductsValue = false
        )
    }?.toMutableList() ?: emptyList<ShopPageItem.ExtraOption.OptionValue>().toMutableList()
    val sex = products.genderList?.map {
        ShopPageItem.ExtraOption.OptionValue(
            id = it.product.id,
            optionValue = it.gender,
            isProductsValue = false
        )
    }?.toMutableList()
    val currentSize = products.sizeValue?.name?.let {
        ShopPageItem.ExtraOption.OptionValue(
            id = products.product.id,
            optionValue = it,
            isProductsValue = true
        )
    }
    val currentColorTaste = products.productColorTasteValue?.let {
        ShopPageItem.ExtraOption.OptionValue(
            id = products.product.id,
            optionValue = it,
            isProductsValue = true
        )
    }

    val currentSex = products.productGenderValue?.let {
        ShopPageItem.ExtraOption.OptionValue(
            id = products.product.id,
            optionValue = it,
            isProductsValue = true
        )
    }

    val optionsList = mutableListOf<ShopPageItem.ExtraOption>()

    if (currentSize != null) {
        size?.add(currentSize)
        size?.reverse()
        optionsList.add(
            ShopPageItem.ExtraOption(
                title = "Размер",
                items = size ?: emptyList()
            )
        )
    }

    if (currentColorTaste != null) {
        colorColorTaste.add(currentColorTaste)
        colorColorTaste.reverse()
        optionsList.add(
            ShopPageItem.ExtraOption(
                title = "Вкус/Цвет",
                items = colorColorTaste
            )
        )
    }

    if (currentSex != null) {
        sex?.add(currentSex)
        sex?.reverse()
        optionsList.add(
            ShopPageItem.ExtraOption(
                title = "Пол",
                items = sex ?: emptyList()
            )
        )
    }

    return optionsList
}

