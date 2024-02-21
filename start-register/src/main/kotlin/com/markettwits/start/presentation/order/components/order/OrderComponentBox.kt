package com.markettwits.start.presentation.order.components.order

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.components.OnBackgroundCard
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
    OnBackgroundCard(
        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
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