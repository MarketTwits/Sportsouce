package com.markettwits.core_ui.items.catalog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.markettwits.core_ui.items.components.progress.SauceLinearProgressIndicator
import com.markettwits.core_ui.items.components.selection.SauceSegmentedTabs
import com.markettwits.core_ui.items.components.selection.SauceSwitchRow
import com.markettwits.core_ui.items.components.status.SauceStatusBadge
import com.markettwits.core_ui.items.components.status.SauceStatusTone
import com.markettwits.core_ui.items.components.textField.SauceTextField
import com.markettwits.core_ui.items.theme.SauceTheme

/** Offline comparison of new shared primitives. No product screen or remote state involved. */
@Composable
fun SauceWebPatternsCatalog(darkTheme: Boolean = false) {
    var dark by rememberSaveable(darkTheme) { mutableStateOf(darkTheme) }
    SauceTheme(darkTheme = dark) {
        Surface(color = SauceTheme.colors.background, contentColor = SauceTheme.colors.textPrimary) {
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Основа SportSauce", style = MaterialTheme.typography.headlineSmall)
                SauceSwitchRow("Тёмная тема", dark, { dark = it })
                SauceWebPatternsExamples()
            }
        }
    }
}

@Composable
internal fun SauceWebPatternsExamples() {
    var name by rememberSaveable { mutableStateOf("") }
    var selected by rememberSaveable { mutableIntStateOf(0) }
    var notifications by rememberSaveable { mutableStateOf(true) }
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Поля с постоянной подписью", style = MaterialTheme.typography.titleMedium)
        SauceTextField("Имя участника", name, { name = it }, placeholder = "Как к вам обращаться",
            supportingText = "Будет видно в списке участников")
        SauceTextField("Email", "mail@", {}, errorMessage = "Укажите полный адрес электронной почты")
        SauceTextField("Номер участника", "12345", {}, readOnly = true,
            supportingText = "Поле доступно только для чтения")
        SauceTextField("Недоступное поле", "Редактирование недоступно", {}, enabled = false)
        Text("Короткие вкладки", style = MaterialTheme.typography.titleMedium)
        SauceSegmentedTabs(listOf("Информация", "Участники"), selected, { selected = it })
        Text(if (selected == 0) "Выбрана информация" else "Выбран список участников",
            style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
        SauceSegmentedTabs(listOf("Обзор", "Архив"), 0, {}, enabled = false)
        Text("Переключатели", style = MaterialTheme.typography.titleMedium)
        SauceSwitchRow("Уведомления о стартах", notifications, { notifications = it },
            supportingText = "Напоминать об изменениях и приближении даты")
        SauceSwitchRow("Изменение настройки недоступно", false, {}, enabled = false,
            supportingText = "Состояние нельзя изменить")
        Text("Прогресс", style = MaterialTheme.typography.titleMedium)
        SauceLinearProgressIndicator("Подготовка данных", 0.65f)
        SauceLinearProgressIndicator("Загрузка данных", null)
        Text("Информационные метки", style = MaterialTheme.typography.titleMedium)
        SauceStatusBadge("Бег", showIndicator = false)
        SauceStatusBadge("Регистрация открыта", tone = SauceStatusTone.Success)
    }
}

@Preview
@Composable
private fun LightWebPatternsPreview() = SauceWebPatternsCatalog(darkTheme = false)

@Preview
@Composable
private fun DarkWebPatternsPreview() = SauceWebPatternsCatalog(darkTheme = true)
