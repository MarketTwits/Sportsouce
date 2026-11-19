package com.markettwits.sportsouce.profile.registrations.presentation.management

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SwapHoriz
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderSectionDivider
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.sportsouce.profile.cloud.model.registrations.OutgoingRegistrationTransfer
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderMember
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderType
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent.*
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent.State

@Composable
internal fun RegistrationPaymentForm(sheet: Sheet.Pay, state: State, component: RegistrationManagementComponent) {
    val price = state.price ?: return
    val reRegistration = sheet.order.orderType is StartOrderType.ReRegistration
    val total = price.totalPrice.coerceAtLeast(0)
    var promo by rememberSaveable { mutableStateOf(initialRegistrationPromo(sheet.order)) }
    var consent by rememberSaveable { mutableStateOf(false) }
    val matchesPrice = promo.trim().lowercase() == state.appliedPromo
    val busy = state.submitting || state.loading

    RegistrationPanel(sheet.order.name) {
        if (reRegistration) {
            SheetText("Стоимость изменения рассчитана с учётом предыдущей оплаты.")
            val difference = ((price.reRegistrationFullPrice?.totalPrice ?: total) - total).coerceAtLeast(0)
            if (difference > 0) SheetText("Учтено с предыдущей оплаты: ${registrationMoney(difference.toLong())}")
        } else {
            SheetText("Начальная стоимость: ${registrationMoney(price.priceWithoutDiscount.toLong())}")
            val discount = (price.priceWithoutDiscount.toLong() + price.additionalFieldsPrice - total).coerceAtLeast(0)
            if (discount > 0) {
                val label = if (price.isPersonalDiscountApplied) "Персональная скидка" else "Скидка"
                SheetText("$label: ${registrationMoney(discount)}")
            }
        }
        if (price.additionalFieldsPrice > 0) SheetText("Дополнительные опции: ${registrationMoney(price.additionalFieldsPrice.toLong())}")
        Text("К оплате · ${registrationMoney(total.toLong())}", style = MaterialTheme.typography.titleMedium)
    }
    if (!reRegistration) {
        FormField("Промокод", promo, !busy) { promo = it }
        SauceButton("Пересчитать с промокодом", enabled = !matchesPrice && !state.submitting, loading = state.loading,
            variant = SauceButtonVariant.Outlined, onClick = { component.accept(Intent.ApplyPromo(promo)) })
    }
    CheckLine("Согласен с условиями проведения старта и политикой конфиденциальности", consent, !busy) {
        consent = it
    }
    SauceButton("Политика конфиденциальности", variant = SauceButtonVariant.Text, onClick = {
        component.accept(Intent.OpenUrl("https://sportsauce.ru/confidentiality"))
    })
    SauceButton(
        title = if (total > 0 && price.isPaymentRequired) "Оплатить ${registrationMoney(total.toLong())}" else "Подтвердить регистрацию",
        enabled = consent && matchesPrice && !state.loading,
        loading = state.submitting,
        onClick = { component.submit(Command.PayOrder(sheet.order.id, state.appliedPromo)) },
    )
}

@Composable
internal fun RegistrationSlotActions(member: StartOrderMember, state: State?, component: RegistrationManagementComponent, available: Boolean) {
    val transfer = state?.transfers?.firstOrNull { it.memberStartId == member.id }
    if (transfer != null || available && state?.transfersLoading != true && state?.transfersError == null) {
        OrderSectionDivider()
        SauceActionRow(
            title = if (transfer != null) "Управлять передачей" else "Передать слот",
            subtitle = transfer?.statusLabel,
            icon = Icons.Outlined.SwapHoriz,
            contentPadding = PaddingValues(horizontal = SauceSpacing.large, vertical = SauceSpacing.medium),
            onClick = {
                component.accept(Intent.Open(if (transfer != null) Sheet.TransferStatus(transfer) else Sheet.Transfer(member)))
            },
        )
    }
}

@Composable
internal fun TransferStatusForm(sheet: Sheet.TransferStatus, state: State, component: RegistrationManagementComponent) {
    val transfer = sheet.invitation
    val fee = transfer.feeKopecks ?: 0
    var confirmRevoke by rememberSaveable { mutableStateOf(false) }
    val busy = state.submitting

    RegistrationPanel(transfer.statusLabel) {
        SheetText(if (transfer.status == "AWAITING_FEE") {
            "Получатель принял слот. Оплатите комиссию, чтобы завершить передачу."
        } else "Слот остаётся у вас, пока получатель не ответит.")
        transfer.expiresAt?.let { SheetText("Срок действия: ${displayRegistrationDeadline(it)}") }
        if (transfer.keptOptions.isNotEmpty()) {
            SheetText("Остаются у вас: " + transfer.keptOptions.joinToString { listOfNotNull(it.name, it.variant).joinToString(" · ") })
        }
    }

    if (confirmRevoke) {
        SheetText("После отзыва слот останется у вас.")
        SauceButton("Подтвердить отзыв", variant = SauceButtonVariant.Destructive, loading = busy,
            onClick = { component.submit(Command.RevokeTransfer(transfer.id)) })
        SauceButton("Назад", enabled = !busy, variant = SauceButtonVariant.Text, onClick = { confirmRevoke = false })
    } else {
        if (transfer.status == "AWAITING_FEE") {
            SauceButton("Оплатить картой · ${registrationMoney(fee.toLong())}", loading = busy,
                onClick = { component.submit(Command.PayTransfer(transfer.id, "card")) })
            state.walletError?.let { error ->
                InlineRegistrationError(error, title = "Не удалось загрузить баланс")
                SauceButton("Обновить баланс", variant = SauceButtonVariant.Text, enabled = !busy,
                    onClick = { component.accept(Intent.RetryWallet) })
            }
            state.walletBalance?.takeIf { it >= fee }?.let { balance ->
                SheetText("Доступно на балансе: ${registrationMoney(balance.toLong())}")
                SauceButton("Оплатить с баланса", enabled = !busy, variant = SauceButtonVariant.Outlined,
                    onClick = { component.submit(Command.PayTransfer(transfer.id, "wallet")) })
            }
        }
        SauceButton("Отозвать запрос", enabled = !busy, variant = SauceButtonVariant.Text, onClick = { confirmRevoke = true })
    }
}

internal val OutgoingRegistrationTransfer.statusLabel: String
    get() = if (status == "AWAITING_FEE") "Ожидает оплаты комиссии" else "Ожидает ответа получателя"
