package com.markettwits.sportsouce.profile.registrations.presentation.management

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.components.selection.SauceCheckboxRow
import com.markettwits.core_ui.items.components.selection.SauceRadioRow
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderSectionCard
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs.OrderSectionHeading
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.status.SauceStatusBadge
import com.markettwits.core_ui.items.components.status.SauceStatusTone
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.sportsouce.profile.cloud.model.registrations.RegistrationOption
import com.markettwits.sportsouce.profile.cloud.model.registrations.RegistrationOptions
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent.*
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent.State
import kotlinx.serialization.json.*

@Composable
internal fun RegistrationOptionsForm(sheet: Sheet.Options, state: State, component: RegistrationManagementComponent) {
    val data = state.options ?: return
    if (data.paidOptions.isEmpty() && data.options.isEmpty() && data.freeAnswers.isEmpty()) {
        SheetText("У этой регистрации нет дополнительных опций.")
        return
    }
    val busy = state.submitting
    val edits = remember(data) { mutableStateMapOf<String, JsonElement?>() }
    var editing by remember(data) { mutableStateOf<String?>(null) }
    fun member(id: Int): String? = sheet.order.members.takeIf { it.size > 1 }?.find { it.id == id }
        ?.let { "${it.name} ${it.surname}" }

    RegistrationOptionsCheckout(
        data = data,
        busy = busy,
        member = ::member,
        onPay = { component.submit(Command.PayOptions(sheet.order.id, it)) },
        onPrivacy = { component.accept(Intent.OpenUrl("https://sportsauce.ru/confidentiality")) },
    )

    if (data.paidOptions.isNotEmpty()) OrderSectionHeading("Оплаченные опции")
    data.paidOptions.forEach { option ->
        key(option.key) {
            RegistrationPanel(option.name) {
                member(option.memberStartId)?.let { SheetText(it) }
                option.displayValue?.takeIf { it.isNotBlank() }?.let { SheetText(it) }
                SauceStatusBadge("Оплачено", tone = SauceStatusTone.Success)
                val change = option.pendingChangeRequest
                when (change?.status) {
                    "pending" -> SauceStatusBadge(
                        change.displayNewValue?.let { "На проверке · $it" } ?: "Изменение на проверке",
                        tone = SauceStatusTone.Warning,
                    )
                    "awaiting_payment" -> {
                        SheetText("Изменение одобрено. Для применения нужна доплата.")
                        change.displayNewValue?.let { SheetText("Новое значение: $it") }
                        val price = change.surchargeAmount?.let { " · ${registrationMoney(it.toLong())}" }.orEmpty()
                        SauceButton("Внести доплату$price", loading = busy, onClick = { component.submit(Command.PayChange(change.id)) })
                    }
                    "rejected" -> InlineRegistrationError(
                        change.rejectionReason?.takeIf { it.isNotBlank() }?.let { "Изменение отклонено: $it" } ?: "Изменение отклонено",
                    )
                }
                if (option.answerId != null && change?.status !in setOf("pending", "awaiting_payment")) {
                    if (editing == option.key) {
                        val value = edits.getOrElse(option.key) { option.initialValue() }
                        OptionValueEditor(option, value, !busy) { edits[option.key] = it }
                        val valid = option.validValue(value) || (option.answerKey == "bool" && value is JsonPrimitive && value.booleanOrNull != null)
                        SauceButton("Запросить изменение", enabled = valid && value != option.initialValue(),
                            loading = busy, onClick = { value?.let { component.submit(Command.ChangeOption(sheet.order.id, option, it)) } })
                        SauceButton("Отменить", enabled = !busy, variant = SauceButtonVariant.Text, onClick = { editing = null })
                    } else SauceButton("Изменить опцию", enabled = !busy, variant = SauceButtonVariant.Soft, onClick = { editing = option.key })
                }
            }
        }
    }
    if (data.freeAnswers.isNotEmpty()) OrderSectionHeading("Ответы без доплаты")
    data.freeAnswers.forEach { answer ->
        RegistrationPanel(answer.name) {
            member(answer.memberStartId)?.let { SheetText(it) }
            answer.displayValue?.let { SheetText(it) }
            SauceStatusBadge("Без доплаты", showIndicator = false)
        }
    }
}

@Composable
internal fun RegistrationOptionsCheckout(
    data: RegistrationOptions,
    busy: Boolean,
    member: (Int) -> String?,
    onPay: (List<Pair<RegistrationOption, JsonElement?>>) -> Unit,
    onPrivacy: () -> Unit,
) {
    val unpaid = data.unpaidOptions
    val available = data.options.filter { it.status == "available" }
    if (unpaid.isEmpty() && available.isEmpty()) return

    val selected = remember(data) { mutableStateMapOf(*unpaid.map { it.key to true }.toTypedArray()) }
    val values = remember(data) { mutableStateMapOf<String, JsonElement?>() }
    var showAvailable by remember(data) { mutableStateOf(unpaid.isEmpty()) }
    var consent by remember(data) { mutableStateOf(false) }

    val entries = (unpaid + available).filter { selected[it.key] == true }
        .map { it to values.getOrElse(it.key) { it.initialValue() } }
    val total = entries.sumOf { (option, value) -> option.priceFor(value).toLong() }
    val valid = entries.all { (option, value) -> !option.needsAnswer || option.validValue(value) }

    if (unpaid.isNotEmpty()) {
        OrderSectionHeading("Неоплаченные опции")
        SheetText("Отметьте опции, которые хотите оплатить сейчас.")
        unpaid.forEach { option ->
            key(option.key) {
                OrderSectionCard {
                    SauceCheckboxRow(
                        label = option.name,
                        supportingText = listOfNotNull(member(option.memberStartId),
                            option.price?.toLong()?.let(::registrationMoney)).joinToString(" · ").ifBlank { null },
                        checked = selected[option.key] == true,
                        onCheckedChange = { selected[option.key] = it },
                        enabled = !busy,
                        modifier = Modifier.padding(SauceSpacing.small),
                    )
                }
            }
        }
    }

    if (available.isNotEmpty()) {
        if (unpaid.isNotEmpty()) {
            val selectedCount = available.count { selected[it.key] == true }
            OrderSectionCard {
                SauceActionRow(
                    title = "Добавить опции",
                    subtitle = if (selectedCount == 0) "Доступно: ${available.size}" else "Выбрано: $selectedCount",
                    icon = Icons.Outlined.ShoppingBag,
                    onClick = { showAvailable = !showAvailable },
                    modifier = Modifier.semantics {
                        stateDescription = if (showAvailable) "Развёрнуто" else "Свёрнуто"
                    },
                    trailingIcon = if (showAvailable) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                    contentPadding = PaddingValues(SauceSpacing.medium),
                )
            }
        } else OrderSectionHeading("Доступно к заказу")
        if (showAvailable) available.forEach { option ->
            key(option.key) {
                val value = values.getOrElse(option.key) { option.initialValue() }
                val checked = selected[option.key] == true
                val checkboxOption = option.needsAnswer && option.answerKey == "bool"
                RegistrationPanel(option.name) {
                    member(option.memberStartId)?.let { SheetText(it) }
                    if (checkboxOption || !checked || !option.needsAnswer) OptionDescription(option)
                    val price = option.priceFor(value).toLong()
                    SauceCheckboxRow(
                        label = "Добавить к оплате",
                        supportingText = if (price > 0 || checkboxOption) registrationMoney(price) else null,
                        checked = checked,
                        enabled = !busy,
                        onCheckedChange = {
                            selected[option.key] = it
                            if (checkboxOption) values[option.key] = JsonPrimitive(it)
                        },
                    )
                    if (checked && option.needsAnswer && !checkboxOption) {
                        OptionValueEditor(option, value, !busy) { values[option.key] = it }
                    }
                }
            }
        }
    }

    RegistrationPanel("К оплате за опции") {
        if (entries.isEmpty()) {
            SheetText("Выберите хотя бы одну опцию.")
        } else {
            Text(registrationMoney(total), style = MaterialTheme.typography.headlineSmall, color = SauceTheme.colors.textPrimary)
            if (!valid) SheetText("Заполните поля выбранных опций.")
        }
        SauceCheckboxRow("Согласен с условиями оплаты и обработкой персональных данных", consent,
            onCheckedChange = { consent = it }, enabled = !busy)
        SauceButton("Политика конфиденциальности", onPrivacy, variant = SauceButtonVariant.Text)
        SauceButton(
            title = if (entries.isEmpty() || total > 0) payRegistrationOptionsLabel(total) else "Сохранить опции",
            enabled = entries.isNotEmpty() && valid && consent,
            loading = busy,
            onClick = { onPay(entries) },
        )
    }
}

@Composable
private fun OptionValueEditor(option: RegistrationOption, value: JsonElement?, enabled: Boolean, change: (JsonElement?) -> Unit) {
    OptionDescription(option)
    when (option.answerKey) {
        "bool" -> CheckLine("Да", (value as? JsonPrimitive)?.booleanOrNull == true, enabled) { change(JsonPrimitive(it)) }
        "singleSelect", "multiSelect" -> {
            val multiple = option.answerKey == "multiSelect"
            val selections = if (multiple) (value as? JsonArray).orEmpty().mapNotNull { (it as? JsonPrimitive)?.intOrNull }.toSet()
                else setOfNotNull((value as? JsonPrimitive)?.intOrNull)
            Column(if (multiple) Modifier else Modifier.selectableGroup()) {
                option.field.options.forEach { choice ->
                    val chosen = choice.id in selections
                    val selectable = enabled && (choice.stockLeft != 0 || chosen && multiple)
                    val price = choice.price?.takeIf { it > 0 }?.let { registrationMoney(it.toLong()) }
                    val stock = choice.stockLeft?.let { if (it == 0) "Нет в наличии" else "Осталось $it" }
                    val supportingText = listOfNotNull(price, stock, choice.description?.takeIf(String::isNotBlank))
                        .joinToString(" · ").ifBlank { null }
                    if (multiple) SauceCheckboxRow(choice.title, chosen, enabled = selectable, supportingText = supportingText,
                        onCheckedChange = {
                            val ids = if (it) selections + choice.id else selections - choice.id
                            change(JsonArray(ids.map(::JsonPrimitive)))
                        })
                    else SauceRadioRow(choice.title, chosen, { change(JsonPrimitive(choice.id)) },
                        enabled = selectable, supportingText = supportingText)
                    if (chosen) OptionImages(choice.images)
                }
            }
        }
        "date" -> RegistrationDateField((value as? JsonPrimitive)?.contentOrNull.orEmpty(), enabled) {
            change(JsonPrimitive(it))
        }
        else -> {
            var text by remember(option.key) { mutableStateOf((value as? JsonPrimitive)?.contentOrNull.orEmpty()) }
            val label = when (option.type) {
                "Время" -> "Время (ЧЧ:ММ)"
                else -> option.field.name
            }
            OutlinedTextFieldBase(
                Modifier.fillMaxWidth(), label, text, isEnabled = enabled,
                keyboardOptions = KeyboardOptions(keyboardType = if (option.type == "Число") KeyboardType.Decimal else KeyboardType.Text),
            ) {
                text = it
                change(if (option.type == "Число") it.replace(',', '.').toDoubleOrNull()
                    ?.takeIf(Double::isFinite)?.let(::JsonPrimitive) else JsonPrimitive(it))
            }
        }
    }
}

@Composable
private fun OptionDescription(option: RegistrationOption) {
    option.field.description?.takeIf { it.isNotBlank() }?.let { SheetText(it) }
    OptionImages(option.field.images)
}

@Composable
private fun OptionImages(images: JsonElement?) {
    val urls = remember(images) {
        val values = when (images) {
            is JsonArray -> images
            is JsonPrimitive -> images.contentOrNull?.let { raw ->
                runCatching { Json.parseToJsonElement(raw) as? JsonArray }.getOrNull() ?: listOf(images)
            }.orEmpty()
            else -> emptyList()
        }
        values.mapNotNull {
            when (it) {
                is JsonPrimitive -> it.contentOrNull
                is JsonObject -> (it["fullPath"] as? JsonPrimitive)?.contentOrNull
                else -> null
            }?.takeIf { url -> url.startsWith("https://") || url.startsWith("http://") }
        }
    }
    urls.forEach { url ->
        AsyncImage(url, "Изображение опции", Modifier.fillMaxWidth().heightIn(max = 180.dp), contentScale = ContentScale.Fit)
    }
}

private fun RegistrationOption.initialValue(): JsonElement? {
    val stored = currentValue?.get(answerKey)?.takeUnless { it == JsonNull } ?: return null
    return if (answerKey == "date" && stored is JsonPrimitive) JsonPrimitive(stored.content.take(10)) else stored
}
