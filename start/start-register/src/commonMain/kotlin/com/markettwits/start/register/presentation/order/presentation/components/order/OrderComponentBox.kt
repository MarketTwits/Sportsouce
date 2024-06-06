package com.markettwits.start.register.presentation.order.presentation.components.order

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.OnBackgroundCard
import com.markettwits.start.register.presentation.order.domain.OrderPrice
import com.markettwits.start.register.presentation.order.presentation.store.OrderStore
import com.markettwits.start.register.presentation.promo.components.RegistrationButton

@Composable
fun OrderComponentBox(
    modifier: Modifier = Modifier,
    price: OrderPrice,
    paymentDisabled: Boolean,
    politics: Boolean,
    button: OrderStore.State.Button,
    onClickCheckRules: () -> Unit,
    onClickRegistry: () -> Unit,
    onClickPrivacyPolicy: (String) -> Unit
) {
    OnBackgroundCard(
        modifier = modifier,
        shape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp,
            bottomEnd = 20.dp,
            bottomStart = 20.dp
        )
    ) {
        if (!paymentDisabled) {
            OrderPriceInfo(
                modifier = Modifier.padding(10.dp),
                membersCount = price.membersCount,
                discountCombo = price.discountComboInCache,
                discountPromo = price.discountPromoInCache,
                discountAge = price.discountAgeInCache,
                total = price.total
            )
        }
        OrderCheckRulesBox(
            isChecked = politics,
            onClickRulesCheck = {
                onClickCheckRules()
            },
            onClickPrivacyPolicy = onClickPrivacyPolicy
        )
        RegistrationButton(
            modifier = Modifier.padding(10.dp),
            isEnabled = button.isEnabled,
            isLoading = button.isLoading,
            title = "Зарегестрироваться",
            onClick = {
                onClickRegistry()
            })
    }
}