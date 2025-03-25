package com.markettwits.sportsouce.shop.order.data.mapper

import com.markettwits.sportsouce.shop.cart.domain.ShopItemCart
import com.markettwits.sportsouce.shop.cloud.model.check.request.CheckShopOrderRequest
import com.markettwits.sportsouce.shop.cloud.model.check.response.CheckShopOrderResponseItem
import com.markettwits.sportsouce.shop.cloud.model.order.request.ShopOrderItemWithCount
import com.markettwits.sportsouce.shop.cloud.model.order.request.ShopOrderItemWithPrice
import com.markettwits.sportsouce.shop.cloud.model.product.ProductRemote
import com.markettwits.sportsouce.shop.order.domain.model.ShopItemOrderResult

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
