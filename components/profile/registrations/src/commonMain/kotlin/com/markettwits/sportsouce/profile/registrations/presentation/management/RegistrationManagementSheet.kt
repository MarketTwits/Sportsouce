package com.markettwits.sportsouce.profile.registrations.presentation.management

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderSectionCard
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderSectionHeading
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.components.selection.SauceCheckboxRow
import com.markettwits.core_ui.items.components.status.SauceStatusBadge
import com.markettwits.core_ui.items.components.status.SauceStatusTone
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.cloud.model.registrations.*
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent.*
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent.State
import kotlinx.serialization.json.*

@Composable
internal fun RegistrationManagementFeedback(component: RegistrationManagementComponent, snackbar: SnackbarHostState) {
    val state by component.state.collectAsState()
    val notice = state.notice
    LaunchedEffect(notice) {
        if (notice != null) {
            snackbar.showSnackbar(notice, withDismissAction = true)
            component.accept(Intent.ClearNotice)
        }
    }
    val sheet = state.sheet ?: return
    DefaultModalBottomSheet(
        title = sheet.title,
        dismissEnabled = !state.submitting,
        onDismissRequest = { component.accept(Intent.Close) },
    ) {
        key(sheet) {
            if (sheet is Sheet.External) {
                ExternalRegistrationForm(sheet.entry, state.submitting, component::submit, serverError = state.error)
            } else {
                val scroll = rememberScrollState()
                LaunchedEffect(state.error) { if (state.error != null) scroll.scrollTo(0) }
                Column(
                    Modifier.fillMaxWidth().sauceVerticalScroll(scroll)
                        .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    state.error?.let { InlineRegistrationError(it) }
                    val waitingForContent = when (sheet) {
                        Sheet.Find -> state.candidates == null
                        is Sheet.Pay -> state.price == null
                        is Sheet.Refund -> state.refundRules == null
                        is Sheet.Transfer -> state.transferOptions == null
                        is Sheet.Options -> state.options == null
                        else -> false
                    }
                    if (state.paymentLink != null) {
                        SauceButton("Открыть страницу оплаты", onClick = { component.accept(Intent.ReopenPayment) })
                    } else if (waitingForContent) {
                        if (state.loading) LinearProgressIndicator(Modifier.fillMaxWidth())
                        else SauceButton("Повторить", onClick = { component.accept(Intent.Retry) })
                    } else {
                        when (sheet) {
                            Sheet.Find -> FindRegistrationsForm(state, component)
                            is Sheet.External -> Unit
                            is Sheet.Delete -> {
                                SheetText("«${sheet.entry.startName}» будет удалён из ваших внешних регистраций.")
                                SauceButton("Удалить", variant = SauceButtonVariant.Destructive, loading = state.submitting,
                                    onClick = { component.submit(Command.DeleteExternal(sheet.entry.id)) })
                                SauceButton("Отмена", enabled = !state.submitting, variant = SauceButtonVariant.Text,
                                    onClick = { component.accept(Intent.Close) })
                            }
                            is Sheet.Correct -> CorrectionForm(sheet.member, state.submitting, component::submit)
                            is Sheet.Transfer -> TransferForm(sheet, state, component)
                            is Sheet.Pay -> RegistrationPaymentForm(sheet, state, component)
                            is Sheet.TransferStatus -> TransferStatusForm(sheet, state, component)
                            is Sheet.Refund -> RefundForm(sheet, state, component::submit)
                            is Sheet.Options -> RegistrationOptionsForm(sheet, state, component)
                        }
                    }
                }
            }
        }
    }
}

internal fun RegistrationManagementComponent.submit(command: Command) = accept(Intent.Submit(command))

@Composable
private fun FindRegistrationsForm(state: State, component: RegistrationManagementComponent) {
    val candidates = state.candidates.orEmpty()
    if (candidates.isEmpty()) {
        SheetText("Регистрации, которые можно привязать к вашему профилю, не найдены.")
        return
    }
    var selected by remember(candidates) { mutableStateOf(emptySet<Int>()) }
    SheetText("Нашли регистрации с вашими данными. Выберите те, которые хотите добавить в профиль.")
    candidates.forEach { item ->
        RegistrationPanel(item.start?.name ?: "Регистрация #${item.id}") {
            val details = listOfNotNull((item.start?.date ?: item.start?.time)?.take(10)?.let(::displayRegistrationDate), item.distance)
            if (details.isNotEmpty()) SheetText(details.joinToString(" · "))
            CheckLine(
                listOfNotNull(listOfNotNull(item.name, item.surname).joinToString(" ").ifBlank { null }, item.team)
                    .joinToString(" · ").ifBlank { "Добавить регистрацию" },
                item.id in selected,
                !state.submitting,
            ) { selected = if (it) selected + item.id else selected - item.id }
        }
    }
    SauceButton("Добавить к себе (${selected.size})", enabled = selected.isNotEmpty(), loading = state.submitting,
        onClick = { component.submit(Command.Attach(selected)) })
}

@Composable
private fun CorrectionForm(member: com.markettwits.sportsouce.profile.registrations.domain.StartOrderMember, busy: Boolean, submit: (Command) -> Unit) {
    var name by rememberSaveable { mutableStateOf(member.name) }
    var surname by rememberSaveable { mutableStateOf(member.surname) }
    val originalYear = member.birthday?.take(4)?.toIntOrNull()
    var year by rememberSaveable { mutableStateOf(originalYear?.toString().orEmpty()) }
    var reason by rememberSaveable { mutableStateOf("") }
    val yearValue = year.toIntOrNull()
    val validYear = year.isBlank() || (year.length == 4 && yearValue != null && yearValue in 1900..currentRegistrationYear())
    val changedYear = yearValue != null && yearValue != originalYear
    val changed = name.trim() != member.name || surname.trim() != member.surname || changedYear
    SheetText("Уточните имя, фамилию или год рождения. Изменения проверит администратор.")
    FormField("Имя", name, !busy) { name = it }
    FormField("Фамилия", surname, !busy) { surname = it }
    OutlinedTextFieldBase(
        Modifier.fillMaxWidth(), "Год рождения", year, isEnabled = !busy, isError = !validYear,
        errorMessage = if (!validYear) "Укажите год четырьмя цифрами" else null,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    ) { year = it.filter(Char::isDigit).take(4) }
    OutlinedTextFieldBase(Modifier.fillMaxWidth(), "Причина (необязательно)", reason, isEnabled = !busy,
        singleLine = false, maxLines = 4) { reason = it }
    SauceButton("Отправить запрос", enabled = name.isNotBlank() && surname.isNotBlank() && validYear && changed,
        loading = busy, onClick = {
            submit(Command.Correct(RegistrationCorrection(requireNotNull(member.id), if (changedYear) "birth_year" else "fio",
                name.trim(), surname.trim(), reason.trim(), yearValue)))
        })
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TransferForm(sheet: Sheet.Transfer, state: State, component: RegistrationManagementComponent) {
    val options = state.transferOptions ?: return
    options.blocked?.takeIf { it.isNotBlank() }?.let {
        SauceStatusBadge(it, tone = SauceStatusTone.Warning)
        return
    }
    var step by rememberSaveable { mutableIntStateOf(0) }
    var mode by rememberSaveable { mutableStateOf<String?>(null) }
    var query by rememberSaveable { mutableStateOf("") }
    var searchedQuery by rememberSaveable { mutableStateOf("") }
    var invite by rememberSaveable { mutableStateOf(false) }
    val decisions = remember(options) { mutableStateMapOf<Int, String>() }
    val carriedCount = options.options.count { it.goesWithSlot || decisions[it.answerId] == "transfer" }
    val found = state.recipient.takeIf { searchedQuery == query.trim() }
    val recipient = found?.recipient?.takeIf { found.found && found.refused.isNullOrBlank() }
    val inviteEmail = found?.inviteEmail?.takeIf { found.canInvite && invite }
    val fee = registrationMoney(options.feeKopecks.toLong())
    val transferTitle = if (mode == "gift") "Подарить слот" else "Рассчитались отдельно"
    Text("Шаг ${step + 1} из 4", style = MaterialTheme.typography.labelLarge, color = SauceTheme.colors.action)
    when (step) {
        0 -> {
            SheetText("Комиссию $fee оплачивает отправитель после того, как получатель примет приглашение.")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChipBase(selected = mode == "gift", label = "Подарить слот", onClick = { mode = "gift" })
                FilterChipBase(selected = mode == "self_settled", label = "Рассчитались отдельно", onClick = { mode = "self_settled" })
            }
            if (mode == "self_settled") SheetText("При возврате деньги получает тот, кто первоначально оплатил слот на сайте.")
        }
        1 -> {
            FormField("Email, телефон или ID", query, !state.loading) { query = it; invite = false }
            SauceButton("Найти получателя", enabled = query.isNotBlank(), loading = state.loading,
                variant = SauceButtonVariant.Soft, onClick = {
                    searchedQuery = query.trim()
                    invite = false
                    component.accept(Intent.FindRecipient(query))
                })
            found?.let { match ->
                when {
                    match.found && match.recipient != null -> RegistrationPanel(match.recipient!!.maskedName) {
                        SheetText(match.refused ?: if (match.checkedOnAnswer) "Допуск к дистанции проверим при ответе." else "Подходит на выбранную дистанцию.")
                    }
                    match.canInvite && match.inviteEmail != null -> {
                        SheetText("Пользователь ещё не зарегистрирован.")
                        CheckLine("Отправить приглашение на ${match.inviteEmail}", invite, true) { invite = it }
                    }
                    else -> SheetText("Получатель не найден. Проверьте введённые данные.")
                }
            }
        }
        2 -> {
            if (options.options.isEmpty()) SheetText("У этого слота нет оплаченных опций для передачи.")
            else SheetText("Выберите, какие опции перейдут получателю, а какие останутся у вас.")
            options.options.forEach { option ->
                RegistrationPanel(listOfNotNull(option.name, option.variant).joinToString(" · ")) {
                    SheetText(registrationMoney(option.priceKopecks.toLong()))
                    if (option.goesWithSlot) SheetText("Передаётся вместе со слотом")
                    else FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChipBase(selected = decisions[option.answerId] == "transfer", label = "Передать",
                            onClick = { decisions[option.answerId] = "transfer" })
                        FilterChipBase(selected = decisions[option.answerId] == "keep", label = "Оставить себе",
                            onClick = { decisions[option.answerId] = "keep" })
                    }
                }
            }
        }
        3 -> {
            RegistrationPanel(transferTitle) {
                SheetText("Получатель: ${recipient?.maskedName ?: inviteEmail.orEmpty()}")
                SheetText("Комиссия: $fee")
                SheetText("Опций к передаче: $carriedCount")
                options.expiresAt?.let { SheetText("Ответ до ${displayRegistrationDeadline(it)}") }
            }
            SheetText("Слот остаётся у вас до принятия приглашения и оплаты комиссии, если она предусмотрена.")
        }
    }
    SauceButton(
        title = if (step == 3) "Отправить приглашение" else "Продолжить",
        enabled = !state.loading && mode != null && (step != 1 || recipient != null || inviteEmail != null) &&
            (step != 2 || options.options.filterNot { it.goesWithSlot }.all { it.answerId in decisions }),
        loading = state.submitting,
        onClick = {
            if (step < 3) step++
            else component.submit(Command.Transfer(requireNotNull(sheet.member.id), RegistrationTransfer(
                requireNotNull(mode), options.options.filterNot { it.goesWithSlot }.map { option ->
                    RegistrationTransfer.Option(option.answerId, decisions[option.answerId] ?: "keep")
                }, targetUserId = recipient?.id, inviteEmail = inviteEmail,
            )))
        },
    )
    if (step > 0) SauceButton("Назад", enabled = !state.submitting && !state.loading,
        variant = SauceButtonVariant.Text, onClick = { step-- })
}

@Composable
private fun RefundForm(sheet: Sheet.Refund, state: State, submit: (Command) -> Unit) {
    val rules = state.refundRules ?: return
    val current = rules.applicableRule(sheet.order)
    val percent = current?.refundPercent?.coerceIn(0, 100) ?: 0
    val paid = sheet.order.paidKopecks
    val paymentGroupId = sheet.order.paymentGroupId
    var confirm by rememberSaveable { mutableStateOf(false) }
    RegistrationPanel(if (confirm) "Подтвердите возврат" else "Условия по текущей дате") {
        SheetText(sheet.order.name)
        if (percent > 0) {
            Text(
                if (paid == null) "К возврату $percent%" else "К возврату ≈ ${registrationMoney(refundAmount(paid, percent))}",
                style = MaterialTheme.typography.titleMedium,
            )
            SheetText("Возврат составляет $percent% оплаченной суммы.")
        } else SheetText("По текущим правилам возврат сейчас недоступен.")
    }
    if (!confirm && rules.rules.isNotEmpty()) RegistrationPanel(
        if (rules.source == "start") "Правила старта" else "Общие правила возврата",
    ) {
        rules.rules.sortedByDescending { it.daysBeforeStart }.forEach { rule ->
            SheetText("${rule.refundPercent}% · не позднее чем за ${rule.daysBeforeStart} дн. до старта")
        }
    }
    if (confirm) SheetText("После возврата регистрация будет отменена.")
    if (percent > 0 && paymentGroupId != null) {
        SauceButton(if (confirm) "Подтвердить возврат" else "Вернуть слот", loading = state.submitting,
            variant = if (confirm) SauceButtonVariant.Destructive else SauceButtonVariant.Outlined,
            onClick = { if (confirm) submit(Command.Refund(sheet.order.id, paymentGroupId, percent)) else confirm = true })
    }
    if (confirm) SauceButton("Назад", enabled = !state.submitting, variant = SauceButtonVariant.Text, onClick = { confirm = false })
}

@Composable
internal fun SheetText(text: String) {
    Text(text, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
}

@Composable
internal fun InlineRegistrationError(message: String, title: String? = null) {
    val colors = SauceTheme.colors.danger
    Surface(Modifier.fillMaxWidth().semantics { liveRegion = LiveRegionMode.Polite },
        color = colors.container, contentColor = colors.content, shape = SauceShapes.field) {
        Row(Modifier.padding(SauceSpacing.medium), horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small),
            verticalAlignment = Alignment.Top) {
            Icon(Icons.Outlined.ErrorOutline, null, Modifier.size(20.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall)) {
                if (title != null) Text(title, style = MaterialTheme.typography.titleSmall)
                Text(message, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
internal fun RegistrationPanel(title: String, content: @Composable ColumnScope.() -> Unit) {
    OrderSectionCard {
        Column(Modifier.padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
            OrderSectionHeading(title)
            content()
        }
    }
}

@Composable
internal fun CheckLine(label: String, checked: Boolean, enabled: Boolean, onChecked: (Boolean) -> Unit) {
    SauceCheckboxRow(label, checked, onChecked, enabled = enabled)
}

@Composable
internal fun FormField(label: String, value: String, enabled: Boolean = true, onValueChange: (String) -> Unit) {
    OutlinedTextFieldBase(Modifier.fillMaxWidth(), label, value, isEnabled = enabled, onValueChange = onValueChange)
}
