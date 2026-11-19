package com.markettwits.sportsouce.profile.registrations.presentation.management

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent.Intent
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent.Sheet

@Composable
internal fun RegistrationCardActions(order: StartOrderInfo, component: RegistrationManagementComponent) {
    val state by component.state.collectAsState()
    if (order.isRefunded || order.id in state.refundedOrderIds) return

    val canPay = order.inlinePaymentAvailable()
    val canRefund = order.canRefund()
    val options = state.optionPreviews[order.id]
    val canManageOptions = !order.isRegistrationPast() && (
        order.hasUnpaidOptions || order.hasPaidOptions ||
            options?.paidOptions?.isNotEmpty() == true || options?.options?.isNotEmpty() == true
        )
    val showOptionsPayment = order.hasOptionsToPay(options)
    if (!canPay && !canRefund && !canManageOptions && !showOptionsPayment) return
    if (showOptionsPayment) {
        SauceButton(payRegistrationOptionsLabel(options?.unpaidAmount()),
            onClick = { component.accept(Intent.Open(Sheet.Options(order))) })
    }
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        if (canPay) TextButton(onClick = { component.accept(Intent.Open(Sheet.Pay(order))) }) {
            Text("Оплатить", color = SauceTheme.colors.action)
        }
        if (canManageOptions && !showOptionsPayment) TextButton(onClick = { component.accept(Intent.Open(Sheet.Options(order))) }) {
            Text("Дополнительные опции", color = SauceTheme.colors.action)
        }
        if (canRefund) TextButton(onClick = { component.accept(Intent.Open(Sheet.Refund(order))) }) {
            Text("Вернуть слот", color = SauceTheme.colors.textSecondary)
        }
    }
}

@Composable
internal fun RegistrationOptionPreview(order: StartOrderInfo, component: RegistrationManagementComponent) {
    if (order.isRefunded || order.isRegistrationPast()) return
    val state by component.state.collectAsState()
    if (order.id in state.refundedOrderIds) return
    LaunchedEffect(order.id, state.previewGeneration) { component.accept(Intent.PreviewOptions(order.id)) }
    val data = state.optionPreviews[order.id] ?: return
    val optionLines = (data.unpaidOptions + data.paidOptions).map { option ->
        val status = when {
            option.status == "unpaid" -> "не оплачено"
            option.pendingChangeRequest?.status == "awaiting_payment" -> "ожидает доплаты"
            option.pendingChangeRequest?.status == "pending" -> "изменение на проверке"
            else -> "оплачено"
        }
        val value = option.displayValue?.trim()?.takeIf { it.isNotEmpty() && !option.name.endsWith(": $it", ignoreCase = true) }
        listOfNotNull(option.name, value, status).joinToString(" · ")
    } + data.freeAnswers.map { answer ->
        listOfNotNull(answer.name, answer.displayValue?.takeIf { it.isNotBlank() }).joinToString(" · ")
    }
    val freeKeys = data.freeAnswers.mapTo(hashSetOf()) { it.memberStartId to it.additionalFieldId }
    val available = data.options.count {
        it.status == "available" && (it.optionId != null || it.memberStartId to it.additionalFieldId !in freeKeys)
    }
    if (optionLines.isEmpty() && available == 0) return

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Дополнительные опции", style = MaterialTheme.typography.labelLarge, color = SauceTheme.colors.textPrimary)
        optionLines.take(2).forEach { SheetText(it) }
        val hidden = optionLines.size - 2
        if (hidden > 0) SheetText("Ещё в регистрации: $hidden")
        if (available > 0) SheetText("Доступно к заказу: $available")
    }
}
