package com.markettwits.sportsouce.shop.order.presentation.components.payment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.selection.SauceRadioRow
import com.markettwits.sportsouce.shop.order.domain.model.ShopDeliveryType
import com.markettwits.sportsouce.shop.order.domain.model.ShopPaymentType
import com.markettwits.sportsouce.shop.order.presentation.components.common.ShopBasicSectorContent

@Composable
internal fun ShopOrderPaymentType(modifier: Modifier = Modifier, shopFilterPaymentTypes: List<ShopPaymentType>,
    selectedShopDeliveryType: ShopDeliveryType, selectedPaymentType: ShopPaymentType,
    onClickChangePaymentType: (ShopPaymentType) -> Unit) {
    ShopBasicSectorContent(modifier, "Способ оплаты") {
        Column(Modifier.selectableGroup()) {
            shopFilterPaymentTypes.filter { it.isAvailable(selectedShopDeliveryType) }.forEach { type ->
                SauceRadioRow(if (type == ShopPaymentType.Cache) "Наличными" else "Онлайн", type == selectedPaymentType,
                    { onClickChangePaymentType(type) })
            }
        }
    }
}
