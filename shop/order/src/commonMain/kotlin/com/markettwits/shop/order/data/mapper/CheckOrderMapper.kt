package com.markettwits.shop.order.data.mapper

import com.markettwits.cloud_shop.model.check.request.CheckShopOrderRequest
import com.markettwits.cloud_shop.model.check.response.CheckShopOrderResponseItem
import com.markettwits.cloud_shop.model.order.request.ShopOrderItemWithCount
import com.markettwits.cloud_shop.model.order.request.ShopOrderItemWithPrice
import com.markettwits.cloud_shop.model.product.ProductRemote
import com.markettwits.shop.cart.domain.ShopItemCart
import com.markettwits.shop.order.domain.model.ShopItemOrderResult

internal suspend fun List<ShopItemCart>.mapCheckOrderRequest(
    transform: suspend (String) -> ProductRemote
) : CheckShopOrderRequest =
    CheckShopOrderRequest(
        items = map {
            ShopOrderItemWithCount(
                product = ShopOrderItemWithPrice(
                    price = it.numberPrice,
                    product = transform(it.item.id).product,
                    productId = it.item.id
                ),
                count = it.count
            )
        }
    )

internal fun List<CheckShopOrderResponseItem>.mapCheckOrderRequest(
    items : List<ShopItemCart>
) : List<ShopItemOrderResult> =
    mapIndexed { index, item ->
        ShopItemOrderResult(
            item = items[index],
            isAvailable = item.success,
            message = item.messages.joinToString(separator = "\n")
        )
    }
