package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.SettingsBackupRestore
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.cloud.model.registrations.RegistrationOptions
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPaymentStatus
import com.markettwits.sportsouce.profile.registrations.presentation.detail.store.StartOrderStore.StartPriceResult
import com.markettwits.sportsouce.profile.registrations.presentation.management.InlineRegistrationError
import com.markettwits.sportsouce.profile.registrations.presentation.management.hasOptionsToPay
import com.markettwits.sportsouce.profile.registrations.presentation.management.inlinePaymentAvailable
import com.markettwits.sportsouce.profile.registrations.presentation.management.registrationMoney

@Composable
internal fun OrderPaymentTab(
    order: StartOrderInfo,
    price: StartPriceResult,
    canOpenPayment: Boolean,
    onPay: () -> Unit,
    onUpdatePrice: () -> Unit,
    onRefund: (() -> Unit)?,
    options: RegistrationOptions? = null,
    onManageOptions: (() -> Unit)? = null,
) {
    val payment = order.payment
    val canPay = order.inlinePaymentAvailable()
    val description = if (order.isRefunded) "Регистрация отменена." else when (payment) {
        is StartOrderPaymentStatus.Success -> payment.paymentReason
        is StartOrderPaymentStatus.Free -> payment.paymentReason
        is StartOrderPaymentStatus.OnPlace -> payment.paymentReason
        else -> null
    }

    OrderDetailPage {
        RegistrationStatusBanner(order, description = description)
        OrderSectionCard {
            Column(Modifier.padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
                val amountLabel = when {
                    order.isRefunded -> "Сумма регистрации"
                    canPay -> "К оплате"
                    order.paymentCode == 7 -> "Уже оплачено"
                    payment is StartOrderPaymentStatus.Success -> "Сумма оплаты"
                    else -> "Стоимость регистрации"
                }
                Text(amountLabel, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
                val amount = when {
                    canPay && price is StartPriceResult.Success -> "${price.price} ₽"
                    canPay && price == StartPriceResult.Free -> "Бесплатно"
                    order.paidKopecks != null && order.paidKopecks > 0 -> registrationMoney(order.paidKopecks.toLong())
                    order.cost.isNotBlank() && order.cost != "0" -> "${order.cost} ₽"
                    payment is StartOrderPaymentStatus.Free -> "Бесплатно"
                    else -> null
                }
                if (amount != null) Text(amount, style = MaterialTheme.typography.headlineSmall, color = SauceTheme.colors.textPrimary)
                if (canPay) {
                    RegistrationPaymentActions(price, canOpenPayment, onPay, onUpdatePrice)
                }
            }
        }

        if (onManageOptions != null && order.hasOptionsToPay(options)) {
            RegistrationOptionsDueBanner(order, options, onManageOptions)
        }
        OrderPaymentBreakdown(order)
        if (onRefund != null) {
            OrderSectionCard {
                SauceActionRow("Вернуть слот", onRefund, icon = Icons.Outlined.SettingsBackupRestore,
                    contentPadding = PaddingValues(SauceSpacing.large))
            }
        }
    }
}

@Composable
internal fun RegistrationPaymentActions(
    price: StartPriceResult,
    canOpenPayment: Boolean,
    onPay: () -> Unit,
    onUpdatePrice: () -> Unit,
) {
    if (price is StartPriceResult.Failed) {
        InlineRegistrationError(price.message)
        SauceButton("Пересчитать стоимость", onUpdatePrice, variant = SauceButtonVariant.Outlined)
    }
    val title = when (price) {
        is StartPriceResult.Success -> "Оплатить ${price.price.replace(' ', '\u00a0')}\u00a0₽"
        StartPriceResult.Free -> "Подтвердить регистрацию"
        else -> "Перейти к оплате"
    }
    SauceButton(title, onPay, loading = !canOpenPayment && price == StartPriceResult.Loading)
}

@Composable
private fun OrderPaymentBreakdown(order: StartOrderInfo) {
    val cost = order.cost.rubles()
    val original = order.costWithoutDiscount.rubles()
    val options = order.additionalFieldsCost.rubles()
    val discount = if (original > 0) (original - (cost - options).coerceAtLeast(0)).coerceAtLeast(0) else 0
    val rows = buildList {
        if (original > 0) add("Начальная стоимость" to "${order.costWithoutDiscount} ₽")
        if (discount > 0) add("Скидка" to "−${registrationMoney(discount * 100)}")
        if (options > 0) add("Дополнительные опции" to "${order.additionalFieldsCost} ₽")
        if (order.promo.isNotBlank()) add("Промокод" to order.promo)
    }
    if (rows.isEmpty()) return

    var expanded by rememberSaveable(order.id) { mutableStateOf(true) }
    OrderSectionCard {
        SauceActionRow("Состав стоимости", { expanded = !expanded },
            modifier = Modifier.semantics { stateDescription = if (expanded) "Развёрнуто" else "Свёрнуто" },
            icon = Icons.Outlined.ReceiptLong, contentPadding = PaddingValues(SauceSpacing.large),
            trailingIcon = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore)
        AnimatedVisibility(expanded) {
            Column {
                OrderSectionDivider()
                Column(Modifier.padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                    rows.forEach { (label, value) -> OrderInfoLine(label, value) }
                }
            }
        }
    }
}

private fun String.rubles(): Long = filterNot(Char::isWhitespace).toLongOrNull() ?: 0
