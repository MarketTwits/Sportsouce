package com.markettwits.core_ui.items.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.core_ui.items.components.buttons.*
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.selection.SauceCheckboxRow
import com.markettwits.core_ui.items.components.selection.SauceRadioRow
import com.markettwits.core_ui.items.components.selection.SauceScrollableTabs
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.core_ui.items.components.divider.BaseDivider
import com.markettwits.core_ui.items.components.status.SauceStatusBadge
import com.markettwits.core_ui.items.components.status.SauceStatusTone
import com.markettwits.core_ui.items.components.textField.CalendarTextFiled
import com.markettwits.core_ui.items.components.textField.ItemsTextFiledDialog
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.components.textField.OutlinePhoneTextFiled
import com.markettwits.core_ui.items.components.timer.screens.TimerDialog
import com.markettwits.core_ui.items.components.timer.screens.rememberTimerPage
import com.markettwits.core_ui.items.screens.AlertDialogScreen
import com.markettwits.core_ui.items.theme.*
import org.jetbrains.compose.ui.tooling.preview.Preview

/** A real Compose catalog for reviewing shared components without network or feature state. */
@Composable
fun SauceComponentCatalog(darkTheme: Boolean = false, initialSheetOpen: Boolean = false) {
    var dark by remember(darkTheme) { mutableStateOf(darkTheme) }
    SauceTheme(darkTheme = dark) {
        var name by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("+7 (913) 456-78-90") }
        var calendarDate by remember { mutableStateOf("15.10.2026") }
        var distance by remember { mutableStateOf("4 км") }
        var selected by remember { mutableStateOf(false) }
        var checked by remember { mutableStateOf(true) }
        var navigation by remember { mutableIntStateOf(1) }
        var radio by remember { mutableIntStateOf(0) }
        var showSheet by remember { mutableStateOf(initialSheetOpen) }
        var showDialog by remember { mutableStateOf(false) }
        var showTimer by remember { mutableStateOf(false) }
        val timer = rememberTimerPage()
        var message by remember { mutableStateOf("") }
        val colors = SauceTheme.colors
        val largeText = LocalDensity.current.fontScale > 1.3f
        Surface(color = colors.background, contentColor = colors.textPrimary) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (largeText) {
                    Text("SportSauce", style = MaterialTheme.typography.headlineMedium)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Компоненты · ${if (dark) "тёмная" else "светлая"} тема",
                            modifier = Modifier.weight(1f), color = colors.textMuted,
                            style = MaterialTheme.typography.bodyMedium)
                        Switch(checked = dark, onCheckedChange = { dark = it })
                    }
                } else Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("SportSauce", style = MaterialTheme.typography.headlineMedium)
                        Text("Компоненты · ${if (dark) "тёмная" else "светлая"} тема", color = colors.textMuted,
                            style = MaterialTheme.typography.bodyMedium)
                    }
                    Switch(checked = dark, onCheckedChange = { dark = it })
                }
                BaseDivider()
                CatalogTitle("Действия")
                SauceButton("Зарегистрироваться", onClick = { message = "Действие выполнено" })
                if (largeText) {
                    SauceButton("Подробнее", onClick = {}, variant = SauceButtonVariant.Outlined)
                    SauceButton("Выбрать", onClick = {}, variant = SauceButtonVariant.Soft)
                    SauceButton("Ссылка", onClick = {}, variant = SauceButtonVariant.Text)
                    SauceButton("Удалить", onClick = { showDialog = true }, variant = SauceButtonVariant.Destructive)
                } else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SauceButton("Подробнее", onClick = {}, modifier = Modifier.weight(1f), variant = SauceButtonVariant.Outlined)
                    SauceButton("Выбрать", onClick = {}, modifier = Modifier.weight(1f), variant = SauceButtonVariant.Soft)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SauceButton("Ссылка", onClick = {}, modifier = Modifier.weight(1f), variant = SauceButtonVariant.Text)
                    SauceButton("Удалить", onClick = { showDialog = true }, modifier = Modifier.weight(1f), variant = SauceButtonVariant.Destructive)
                }
                }
                SauceButton("Недоступно", onClick = {}, enabled = false)
                SauceButton("Загрузка", onClick = {}, loading = true)
                if (message.isNotEmpty()) Text(message, color = colors.success.content)
                CatalogTitle("Поля и выбор")
                OutlinedTextFieldBase(label = "Имя участника", value = name, placeholder = { Text("Как к вам обращаться") }, onValueChange = { name = it })
                OutlinePhoneTextFiled(label = "Телефон", value = phone, onValueChange = { phone = it })
                OutlinedTextFieldBase(label = "Email", value = "mail@", isError = true,
                    supportingText = { Text("Укажите полный адрес электронной почты") }, onValueChange = {})
                OutlinedTextFieldBase(label = "Недоступное поле", value = "Изменения недоступны", isEnabled = false, onValueChange = {})
                ItemsTextFiledDialog(label = "Дистанция", value = distance, items = listOf("4 км", "12 км", "Детский забег"), onValueChanged = { distance = it })
                CalendarTextFiled(value = calendarDate, clearable = true, textFiled = { fieldModifier ->
                    OutlinedTextFieldBase(fieldModifier, "Дата", calendarDate, onValueChange = {})
                }, onValueChanged = { calendarDate = it })
                SauceCheckboxRow("Участвовать в командном зачёте", checked, { checked = it })
                SauceRadioRow("Взрослый", radio == 0, { radio = 0 })
                SauceRadioRow("Ребёнок", radio == 1, { radio = 1 })
                SauceScrollableTabs(listOf("Главная", "Ближайшие", "Прошедшие", "Анонсы"), navigation, { navigation = it })
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChipBase(selected = selected, onClick = { selected = !selected }, label = "Бег")
                    FilterChipBase(selected = true, enabled = false, onClick = {}, label = "Недоступно")
                }
                CatalogTitle("Варианты по мотивам сайта")
                SauceWebPatternsExamples()
                CatalogTitle("Статусы")
                SauceStatusBadge("Регистрация открыта", tone = SauceStatusTone.Success)
                SauceStatusBadge("Регистрация закрыта, ожидайте начало старта", tone = SauceStatusTone.Warning)
                SauceStatusBadge("Не удалось сохранить изменения", tone = SauceStatusTone.Danger)
                SauceStatusBadge("Старт завершён")
                CatalogTitle("Поверхности")
                OnBackgroundCard { inner ->
                    Column(inner.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Страшно добрый забег", style = MaterialTheme.typography.titleMedium)
                        Text("25 октября · Заельцовский парк", color = colors.textSecondary, style = MaterialTheme.typography.bodyMedium)
                        Text("4 км · Детский забег", color = colors.textMuted, style = MaterialTheme.typography.bodySmall)
                        SauceStatusBadge("Регистрация открыта", tone = SauceStatusTone.Success)
                    }
                }
                SauceButton("Открыть нижнюю панель", onClick = { showSheet = true }, variant = SauceButtonVariant.Outlined)
                SauceButton("Выбрать время", onClick = { showTimer = true }, variant = SauceButtonVariant.Outlined)
                Text("Брендовый акцент", color = colors.brand, style = MaterialTheme.typography.labelMedium)
            }
        }
        if (showSheet) DefaultModalBottomSheet(title = "Выберите дистанцию", onDismissRequest = { showSheet = false }) {
            Column(Modifier.padding(start = 20.dp, end = 20.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SauceStatusBadge("Осталось 16 мест", tone = SauceStatusTone.Success)
                Text("Основная поверхность и поднятая панель различимы в обеих темах.")
                SauceButton("Продолжить", onClick = { showSheet = false })
            }
        }
        if (showDialog) AlertDialogScreen(onDismissRequest = { showDialog = false }, onClickOk = { showDialog = false }, title = "Удалить запись?", text = "Это демонстрация подтверждения действия.")
        TimerDialog(timerState = timer, isShowDialog = showTimer, onDismiss = { showTimer = false },
            onApply = { message = timer.timeState.toString(); showTimer = false })
    }
}

@Composable
private fun CatalogTitle(title: String) {
    Text(title, modifier = Modifier.padding(top = 8.dp), style = MaterialTheme.typography.titleLarge)
}

@Preview
@Composable
private fun LightCatalogPreview() = SauceComponentCatalog(darkTheme = false)

@Preview
@Composable
private fun DarkCatalogPreview() = SauceComponentCatalog(darkTheme = true)
