package com.markettwits.shop.item.domain.mapper

import com.markettwits.cloud_shop.model.product.ProductRemote
import com.markettwits.shop.item.domain.models.ShopPageItem
import java.text.NumberFormat
import java.util.Locale

class ShopPageItemMapperBase : ShopPageItemMapper {

    override fun map(products: ProductRemote): ShopPageItem =
        ShopPageItem(
            id = products.product.id,
            code = products.product.code,
            price = calculatePrice(
                price = products.product.price,
                discountPrice = products.product.discountPrice
            ),
            visual = calculateVisual(products),
            options = calculateOptions(products),
            extraOptions = calculateExtras(products),
            quantity = products.product.quantity,
            fullPathUrl = "https://shop.sportsauce.ru/product/${products.product.id}"
        )
}

private fun calculateExtras(products: ProductRemote): List<ShopPageItem.ExtraOption> {

    val size = products.sizeList.map {
        ShopPageItem.ExtraOption.OptionValue(
            id = it.product.id,
            optionValue = it.size,
            isProductsValue = false
        )
    }.toMutableList()
    val colorColorTaste = products.colorTasteList?.map {
        ShopPageItem.ExtraOption.OptionValue(
            id = it.product.id,
            optionValue = it.colorTaste,
            isProductsValue = false
        )
    }?.toMutableList() ?: emptyList<ShopPageItem.ExtraOption.OptionValue>().toMutableList()
    val sex = products.genderList.map {
        ShopPageItem.ExtraOption.OptionValue(
            id = it.product.id,
            optionValue = it.gender,
            isProductsValue = false
        )
    }.toMutableList()
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
        size.add(currentSize)
        size.reverse()
        optionsList.add(
            ShopPageItem.ExtraOption(
                title = "Размер",
                items = size
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
        sex.add(currentSex)
        sex.reverse()
        optionsList.add(
            ShopPageItem.ExtraOption(
                title = "Пол",
                items = sex
            )
        )
    }

    return optionsList
}


private fun calculateOptions(products: ProductRemote): List<ShopPageItem.Option> =
    products.product.options.map { option ->
        ShopPageItem.Option(
            id = option.id,
            optionTitle = option.option?.name ?: "",
            optionValue = option.value.name,
        )
    }

private fun calculateVisual(product: ProductRemote): ShopPageItem.Visual =
    ShopPageItem.Visual(
        imageUrl = product.product.images?.map { it.file?.fullPath ?: "" } ?: emptyList(),
        description = product.product.description,
        displayName = product.product.shopDisplayName ?: product.product.name
    )

private fun calculatePrice(price: Int, discountPrice: Int?): ShopPageItem.Price {
    val currentPrice: String
        val previousPrice: String?
        val discount: Int?
        return if (discountPrice != null && discountPrice > 0 && discountPrice < price) {
            currentPrice = formatPrice(discountPrice / 100)
            previousPrice = formatPrice(price / 100)
            discount =
                ((price - discountPrice) * 100 / price)
            ShopPageItem.Price(
                currentPrice = currentPrice,
            previousPrice = previousPrice,
            discount = discount
        )
    } else {
        currentPrice = formatPrice(price / 100)
        previousPrice = null
        discount = null
        ShopPageItem.Price(
            currentPrice = currentPrice,
            previousPrice = previousPrice,
            discount = discount
        )
    }
}

private fun formatPrice(price: Int): String {
    val formatter = NumberFormat.getInstance(Locale("ru", "RU"))
    return formatter.format(price)
}
