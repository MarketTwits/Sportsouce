package com.markettwits.sportsouce.shop.order.presentation.components.recipient

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.components.textField.mapSimpleRuPhoneNumberToFull
import com.markettwits.sportsouce.shop.order.domain.model.ShopRecipient
import com.markettwits.sportsouce.shop.order.presentation.components.common.ShopBasicSectorContent

@Composable
internal fun ShopOrderRecipient(modifier: Modifier = Modifier, shopRecipient: ShopRecipient, onClick: () -> Unit) {
    ShopBasicSectorContent(modifier, "Получатель") {
        SauceActionRow(shopRecipient.name.ifBlank { "Указать получателя" }, onClick,
            subtitle = mapSimpleRuPhoneNumberToFull(shopRecipient.phone), icon = Icons.Default.Person)
    }
}
