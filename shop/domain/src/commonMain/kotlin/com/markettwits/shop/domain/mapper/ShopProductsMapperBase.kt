package com.markettwits.shop.domain.mapper

import com.markettwits.cloud_shop.model.product.Product
import com.markettwits.shop.domain.model.ShopItem

class ShopProductsMapperBase : ShopProductsMapper {

    override fun map(products: List<Product>): List<ShopItem> = products.map { map(it) }

    override fun map(product: Product): ShopItem = convertToShopItem(product)

    private fun convertToShopItem(productsRemoteRow: Product): ShopItem {

        return ShopItem(
            id = productsRemoteRow. id,
            code = productsRemoteRow.code,
            price = calculatePrice(
                price = productsRemoteRow.price,
                discountPrice = productsRemoteRow.discountPrice
            ),
            visual = calculateVisual(productsRemoteRow),
            options = calculateOptions(productsRemoteRow),
            quantity = productsRemoteRow.quantity,
            fullPathUrl = "$DEFAULT_SHOP_PRODUCT_PATH${productsRemoteRow.id}"
        )
    }

    private fun calculateOptions(products: Product): List<ShopItem.Option> =
        products.options.map { option ->
            ShopItem.Option(
                id = option.id,
                optionTitle = option.option?.name ?: "",
                optionValue = option.value.name,
            )
        }

    private fun calculateVisual(product: Product): ShopItem.Visual =
        ShopItem.Visual(
            imageUrl = product.images?.map { it.file?.fullPath ?: "" } ?: emptyList(),
            description = product.description,
            displayName = product.shopDisplayName ?: product.name
        )

    private fun calculatePrice(price: Int, discountPrice: Int?): ShopItem.Price {
        val currentPrice: String
        val previousPrice: String?
        val discount: Int?
        return if (discountPrice != null && discountPrice > 0 && discountPrice < price) {
            currentPrice = formatPriceWithSpaces(discountPrice / 100)
            previousPrice = formatPriceWithSpaces(price / 100)
            discount =
                ((price - discountPrice) * 100 / price)
            ShopItem.Price(
                currentPrice = currentPrice,
                previousPrice = previousPrice,
                discount = discount
            )
        } else {
            currentPrice = formatPriceWithSpaces(price / 100)
            ShopItem.Price(
                currentPrice = currentPrice,
                previousPrice = null,
                discount = null
            )
        }
    }

    private companion object{
        private const val DEFAULT_SHOP_PRODUCT_PATH = "https://shop.sportsauce.ru/product/"
    }
}
