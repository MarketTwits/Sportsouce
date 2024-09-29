package com.markettwits.shop.catalog.domain.mapper

import com.markettwits.cloud_shop.model.products.ProductsRemoteRow
import com.markettwits.shop.catalog.domain.models.ShopItem
import java.text.NumberFormat
import java.util.Locale

class ShopProductsMapperBase : ShopProductsMapper {

    override fun map(products: List<ProductsRemoteRow>): List<ShopItem> = products.map { map(it) }

    override fun map(product: ProductsRemoteRow): ShopItem =
        convertToShopItem(product)
}

private fun convertToShopItem(productsRemoteRow: ProductsRemoteRow): ShopItem {

    val currentPrice: String
    val previousPrice: String?
    val discount: Int?

    if (productsRemoteRow.discountPrice != null && productsRemoteRow.discountPrice!! > 0 && productsRemoteRow.discountPrice!! < productsRemoteRow.price) {
        currentPrice = formatPrice(productsRemoteRow.discountPrice!! / 100)
        previousPrice = formatPrice(productsRemoteRow.price / 100)
        discount =
            ((productsRemoteRow.price - productsRemoteRow.discountPrice!!) * 100 / productsRemoteRow.price)
    } else {
        currentPrice = formatPrice(productsRemoteRow.price / 100)
        previousPrice = null
        discount = null
    }

    val imageUrl = productsRemoteRow.images.firstOrNull()?.file?.fullPath ?: ""

    return ShopItem(
        id = productsRemoteRow.id,
        currentPrice = currentPrice,
        previousPrice = previousPrice,
        discount = discount,
        title = productsRemoteRow.name,
        imageUrl = imageUrl
    )
}

private fun formatPrice(price: Int): String {
    val formatter = NumberFormat.getInstance(Locale("ru", "RU"))
    return formatter.format(price)
}
