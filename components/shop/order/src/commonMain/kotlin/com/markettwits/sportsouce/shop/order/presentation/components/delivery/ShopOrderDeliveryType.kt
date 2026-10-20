package com.markettwits.sportsouce.shop.order.presentation.components.delivery

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.selection.SauceRadioRow
import com.markettwits.sportsouce.shop.order.domain.model.ShopDeliveryType
import com.markettwits.sportsouce.shop.order.presentation.components.common.ShopBasicSectorContent

@Composable
internal fun ShopOrderDeliveryType(modifier: Modifier = Modifier, shopDeliveryType: List<ShopDeliveryType>,
    selectedOption: ShopDeliveryType, onClickOption: (ShopDeliveryType) -> Unit) {
    ShopBasicSectorContent(modifier, "Способ получения") {
        Column(Modifier.selectableGroup()) { shopDeliveryType.forEach { type ->
            SauceRadioRow(type.mapTitleToString(), type == selectedOption, { onClickOption(type) })
        } }
    }
}

@Composable
fun SelectorOption(modifier: Modifier = Modifier, text: String, isSelected: Boolean, onClick: () -> Unit) {
    SauceRadioRow(text, isSelected, onClick, modifier)
}

internal fun ShopDeliveryType.mapTitleToString(): String = when (this) {
    ShopDeliveryType.Delivery -> "Доставка"
    ShopDeliveryType.Pickup -> "Самовывоз"
}
internal fun ShopDeliveryType.mapPlaceToString(): String = when (this) {
    ShopDeliveryType.Delivery -> "Доставка осуществляется с помощью ТК"
    ShopDeliveryType.Pickup -> "Новосибирск, Сухарная 70а"
}
