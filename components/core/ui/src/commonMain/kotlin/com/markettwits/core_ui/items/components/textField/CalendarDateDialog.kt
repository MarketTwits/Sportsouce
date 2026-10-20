package com.markettwits.core_ui.items.components.textField

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.divider.BaseDivider
import com.markettwits.core_ui.items.components.focus.sauceFocusOutline
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSizes
import com.markettwits.core_ui.items.theme.SauceTheme
import kotlinx.datetime.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private val monthNames = listOf("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь")
private val shortMonths = listOf("Янв", "Фев", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек")

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class, ExperimentalLayoutApi::class)
@Composable
internal fun CalendarDateDialog(
    initialSelectedDateMillis: Long?,
    yearRange: IntRange,
    clearable: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Long?) -> Unit,
) {
    val colors = SauceTheme.colors
    val keyboard = LocalSoftwareKeyboardController.current
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val initial = initialSelectedDateMillis?.let(::calendarDate) ?: today
    var selected by rememberSaveable { mutableStateOf(initialSelectedDateMillis) }
    var raw by rememberSaveable { mutableStateOf(initialSelectedDateMillis?.let(::calendarDateText)?.filter(Char::isDigit).orEmpty()) }
    var cleared by rememberSaveable { mutableStateOf(false) }
    var year by rememberSaveable { mutableIntStateOf(initial.year.coerceIn(yearRange)) }
    var month by rememberSaveable { mutableIntStateOf(initial.month.number) }
    var yearPage by rememberSaveable { mutableIntStateOf(year / 12 * 12) }
    fun select(date: LocalDate) {
        selected = date.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
        raw = calendarDateText(selected!!).filter(Char::isDigit)
        cleared = false
    }
    fun dismiss() { keyboard?.hide(); onDismiss() }

    BasicAlertDialog(onDismissRequest = ::dismiss,
        modifier = Modifier.widthIn(max = 368.dp).fillMaxWidth().padding(vertical = 16.dp),
        properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(shape = SauceShapes.card, color = colors.surfaceRaised, contentColor = colors.textPrimary,
            border = BorderStroke(1.dp, colors.border)) {
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val allowCalendar = maxWidth >= 352.dp
                val startWithInput = !allowCalendar || maxHeight < 560.dp || LocalDensity.current.fontScale > 1.3f
                var page by rememberSaveable { mutableStateOf(if (startWithInput) "input" else "days") }
                val mode = if (allowCalendar) page else "input"
                val visibleMonth = LocalDate(year, month, 1)
                Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (mode == "input") Row(Modifier.fillMaxWidth().padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Введите дату",
                            Modifier.weight(1f).semantics { heading() }, style = MaterialTheme.typography.titleMedium)
                        if (allowCalendar) IconButton(onClick = { page = "days" }) {
                            Icon(Icons.Default.CalendarMonth, contentDescription = "Показать календарь", tint = colors.textSecondary)
                        }
                        IconButton(onClick = ::dismiss) { Icon(Icons.Default.Close, "Закрыть", tint = colors.textSecondary, modifier = Modifier.size(20.dp)) }
                    } else {
                        val title = when (mode) {
                            "years" -> "$yearPage – ${yearPage + 11}"
                            "months" -> year.toString()
                            else -> "${monthNames[month - 1]} $year"
                        }
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            if (mode == "years") Text(title, Modifier.weight(1f).padding(horizontal = 8.dp).semantics { heading() },
                                style = MaterialTheme.typography.titleMedium)
                            else TextButton(onClick = {
                                page = if (mode == "days") "months" else "years"
                                yearPage = year / 12 * 12
                            }, colors = ButtonDefaults.textButtonColors(contentColor = colors.action),
                                modifier = Modifier.weight(1f).heightIn(min = SauceSizes.touchTarget).semantics {
                                    heading()
                                    stateDescription = if (mode == "days") "Выбор месяца" else "Выбор года"
                                }) {
                                Text(title, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, color = colors.textPrimary)
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null,
                                    modifier = Modifier.size(20.dp), tint = colors.textSecondary)
                            }
                            val previous = when (mode) {
                                "years" -> yearPage > yearRange.first
                                "months" -> year > yearRange.first
                                else -> year > yearRange.first || month > 1
                            }
                            val next = when (mode) {
                                "years" -> yearPage + 12 <= yearRange.last
                                "months" -> year < yearRange.last
                                else -> year < yearRange.last || month < 12
                            }
                            fun move(direction: Int) {
                                when (mode) {
                                    "years" -> yearPage += direction * 12
                                    "months" -> year += direction
                                    else -> visibleMonth.plus(DatePeriod(months = direction)).let { year = it.year; month = it.month.number }
                                }
                            }
                            IconButton(enabled = previous, onClick = { move(-1) }) {
                                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Предыдущий период")
                            }
                            IconButton(enabled = next, onClick = { move(1) }) {
                                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Следующий период")
                            }
                            IconButton(onClick = ::dismiss) {
                                Icon(Icons.Default.Close, "Закрыть", tint = colors.textSecondary, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                    Column(Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (mode == "input") {
                            val formatted = calendarInputText(raw)
                            val error = if (raw.length < 8) null else if (calendarDateMillis(formatted, 1..9999) == null)
                                "Введите существующую дату" else if (calendarDateMillis(formatted, yearRange) == null)
                                "Выберите год с ${yearRange.first} по ${yearRange.last}" else null
                            OutlinedTextFieldBase(modifier = Modifier.padding(horizontal = 8.dp), label = "Дата", value = raw,
                                isError = error != null,
                                visualTransformation = CalendarInputTransformation,
                                placeholder = { Text("ДД.ММ.ГГГГ") },
                                supportingText = { Text(error ?: "ДД.ММ.ГГГГ", style = MaterialTheme.typography.bodySmall) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = { keyboard?.hide() }),
                                onValueChange = {
                                    raw = it.filter { char -> char in '0'..'9' }.take(8)
                                    selected = calendarDateMillis(calendarInputText(raw), yearRange)
                                    cleared = clearable && raw.isEmpty()
                                    selected?.let { millis -> calendarDate(millis).let { date -> year = date.year; month = date.month.number } }
                                })
                        } else {
                            when (mode) {
                                "years" -> CalendarChoices((yearPage..yearPage + 11).toList(), { it.toString() },
                                    { it == year }, { it in yearRange }) { year = it; page = "months" }
                                "months" -> CalendarChoices((1..12).toList(), { shortMonths[it - 1] },
                                    { it == month }, { true }) { month = it; page = "days" }
                                else -> {
                                    Row(Modifier.fillMaxWidth()) {
                                        listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс").forEach { label ->
                                            Text(label, Modifier.weight(1f).padding(vertical = 4.dp), textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.labelSmall, color = colors.textMuted)
                                        }
                                    }
                                    val selectedDay = selected?.let(::calendarDate)
                                    val start = visibleMonth.minus(DatePeriod(days = visibleMonth.dayOfWeek.ordinal))
                                    Column {
                                        repeat(6) { row ->
                                            Row(Modifier.fillMaxWidth()) {
                                                repeat(7) { column ->
                                                    val date = start.plus(DatePeriod(days = row * 7 + column))
                                                    CalendarCell(date.day.toString(), Modifier.weight(1f),
                                                        selected = selectedDay == date,
                                                        enabled = date.year in yearRange,
                                                        muted = date.month != visibleMonth.month,
                                                        today = date == today,
                                                        description = calendarDateDescription(date),
                                                        compact = true) {
                                                        select(date); year = date.year; month = date.month.number
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        BaseDivider()
                        FlowRow(Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            SauceButton("Сегодня", onClick = { select(today); year = today.year; month = today.month.number },
                                enabled = today.year in yearRange, variant = SauceButtonVariant.Text, fillWidth = false)
                            if (clearable) SauceButton("Очистить", onClick = { selected = null; raw = ""; cleared = true },
                                enabled = selected != null || raw.isNotEmpty(), variant = SauceButtonVariant.Text, fillWidth = false)
                            if (mode != "input") IconButton(onClick = { page = "input" }) {
                                Icon(Icons.Default.Edit, "Ввести дату", tint = colors.textSecondary)
                            }
                        }
                    }
                    FlowRow(Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                        verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SauceButton("Отменить", ::dismiss, variant = SauceButtonVariant.Text, fillWidth = false)
                        SauceButton("ОК", onClick = { keyboard?.hide(); onConfirm(selected) }, fillWidth = false,
                            enabled = selected != null || (clearable && cleared && initialSelectedDateMillis != null))
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarChoices(values: List<Int>, label: (Int) -> String, selected: (Int) -> Boolean,
    enabled: (Int) -> Boolean, onClick: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        values.chunked(3).forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { value -> CalendarCell(label(value), Modifier.weight(1f), selected(value), enabled(value)) { onClick(value) } }
            }
        }
    }
}

@Composable
private fun CalendarCell(text: String, modifier: Modifier, selected: Boolean, enabled: Boolean,
    muted: Boolean = false, today: Boolean = false, description: String? = null, compact: Boolean = false, onClick: () -> Unit) {
    val colors = SauceTheme.colors
    val interactions = remember { MutableInteractionSource() }
    Box(modifier.heightIn(min = SauceSizes.touchTarget).clip(SauceShapes.field)
        .background(if (selected) colors.action else colors.surfaceRaised)
        .border(BorderStroke(if (today && !selected) 1.dp else 0.dp,
            if (today && !selected) colors.action else androidx.compose.ui.graphics.Color.Transparent), SauceShapes.field)
        .selectable(selected = selected, enabled = enabled, role = Role.Button, interactionSource = interactions,
            indication = ripple(color = colors.action), onClick = onClick)
        .sauceFocusOutline(interactions, SauceShapes.field, if (selected) colors.onAction else colors.action, enabled)
        .semantics {
            if (description != null) contentDescription = description
            if (today) stateDescription = "Сегодня"
        }.padding(vertical = if (compact) 4.dp else 12.dp), contentAlignment = Alignment.Center) {
        Text(text, style = if (compact) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodyLarge,
            color = when { !enabled -> colors.disabledContent; selected -> colors.onAction; muted -> colors.textMuted; else -> colors.textPrimary },
            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }
}
