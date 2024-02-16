package com.markettwits.start.presentation.order.components.order

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.start.presentation.order.domain.OrderPrice
import com.markettwits.start.presentation.order.store.OrderStore
import com.markettwits.start.presentation.promo.components.RegistrationButton

@Composable
fun OrderComponentBox(
    modifier: Modifier = Modifier,
    price: OrderPrice,
    paymentDisabled: Boolean,
    politics: Boolean,
    button: OrderStore.State.Button,
    onClickCheckRules: () -> Unit,
    onClickRegistry: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        if (!paymentDisabled) {
            OrderPriceInfo(
                modifier = modifier,
                membersCount = price.membersCount,
                discountInCache = price.discountInCache,
                total = price.total
            )
        }
        OrderCheckRulesBox(isChecked = politics, onClickRulesCheck = {
            onClickCheckRules()
        })
        RegistrationButton(
            modifier = modifier,
            isEnabled = button.isEnabled,
            isLoading = button.isLoading,
            title = "Зарегестрироваться",
            onClick = {
                onClickRegistry()
            })
    }
}