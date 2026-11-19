package com.markettwits.sportsouce.profile.registrations.presentation.management

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll
import com.markettwits.core_ui.items.components.textField.SauceTextField
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.cloud.model.registrations.ExternalRegistration
import com.markettwits.sportsouce.profile.registrations.presentation.management.RegistrationManagementComponent.Command
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
internal expect fun rememberRegistrationPosterPicker(
    onReading: () -> Unit,
    onPicked: (RegistrationPoster?, String?) -> Unit,
): () -> Unit

internal fun currentRegistrationYear() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year

internal fun displayRegistrationDate(value: String): String =
    parseDate(value)?.let { "${it.day.toString().padStart(2, '0')}.${it.month.ordinal.plus(1).toString().padStart(2, '0')}.${it.year}" } ?: value

internal fun displayRegistrationDeadline(value: String): String {
    val dateTime = runCatching { Instant.parse(value).toLocalDateTime(TimeZone.currentSystemDefault()) }.getOrNull()
        ?: return displayRegistrationDate(value.take(10))
    val time = dateTime.time.toString().take(5)
    return "${displayRegistrationDate(dateTime.date.toString())}, $time"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RegistrationDateField(
    value: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    external: Boolean = false,
    error: String? = null,
    onChange: (String) -> Unit,
) {
    var showPicker by rememberSaveable { mutableStateOf(false) }
    val date = if (external) parseExternalRegistrationDate(value) else parseDate(value)
    SauceTextField(
        modifier = modifier.fillMaxWidth(),
        label = if (external) "Дата старта *" else "Дата (ГГГГ-ММ-ДД)",
        value = value,
        enabled = enabled,
        placeholder = if (external) "ДД.ММ.ГГГГ" else null,
        errorMessage = if (external) error else {
            if (value.isNotBlank() && date == null) "Укажите дату в формате ГГГГ-ММ-ДД" else null
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        trailingIcon = {
            IconButton(enabled = enabled, onClick = { showPicker = true }) {
                Icon(Icons.Default.CalendarToday, "Выбрать дату в календаре")
            }
        },
        onValueChange = onChange,
    )
    if (showPicker) {
        val selected = rememberDatePickerState(
            initialSelectedDateMillis = date?.takeIf { it.year in 1900..2100 }
                ?.atStartOfDayIn(TimeZone.UTC)?.toEpochMilliseconds(),
            yearRange = 1900..2100,
        )
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(enabled = selected.selectedDateMillis != null, onClick = {
                    selected.selectedDateMillis?.let { millis ->
                        val iso = Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.UTC).date.toString()
                        onChange(if (external) displayRegistrationDate(iso) else iso)
                    }
                    showPicker = false
                }) { Text("Выбрать") }
            },
            dismissButton = { TextButton(onClick = { showPicker = false }) { Text("Отмена") } },
        ) { DatePicker(selected) }
    }
}

@Composable
internal fun ExternalRegistrationForm(
    entry: ExternalRegistration?,
    busy: Boolean,
    submit: (Command) -> Unit,
    serverError: String? = null,
) {
    var startName by rememberSaveable { mutableStateOf(entry?.startName.orEmpty()) }
    var distanceName by rememberSaveable { mutableStateOf(entry?.distanceName.orEmpty()) }
    var date by rememberSaveable { mutableStateOf(entry?.startDate?.take(10)?.let(::displayRegistrationDate).orEmpty()) }
    var url by rememberSaveable { mutableStateOf(entry?.url.orEmpty()) }
    var ageGroup by rememberSaveable { mutableStateOf(entry?.ageGroup.orEmpty()) }
    var team by rememberSaveable { mutableStateOf(entry?.team.orEmpty()) }
    var posterId by rememberSaveable { mutableStateOf(entry?.posterId ?: entry?.poster?.id) }
    var showExistingPoster by rememberSaveable { mutableStateOf(entry?.poster?.url != null) }
    var poster by remember { mutableStateOf<RegistrationPoster?>(null) }
    var fileError by remember { mutableStateOf<String?>(null) }
    var readingPoster by remember { mutableStateOf(false) }
    var validationAttempt by rememberSaveable { mutableIntStateOf(0) }
    val positions = remember { ExternalRegistrationField.entries.associateWith { BringIntoViewRequester() } }
    val focus = LocalFocusManager.current
    val pick = rememberRegistrationPosterPicker(onReading = {
        readingPoster = true
        fileError = null
    }) { picked, error ->
        readingPoster = false
        if (picked != null) poster = picked
        fileError = error
    }
    val draft = ExternalRegistrationDraft(entry?.id, startName, distanceName, date, url, ageGroup, team, posterId, poster)
    val errors = draft.errors()
    LaunchedEffect(validationAttempt) {
        if (validationAttempt > 0) errors.keys.firstOrNull()?.let { positions.getValue(it).bringIntoView() }
    }

    fun fieldError(field: ExternalRegistrationField) = errors[field]?.takeIf { validationAttempt > 0 }
    fun position(field: ExternalRegistrationField) = Modifier.fillMaxWidth().bringIntoViewRequester(positions.getValue(field))

    val scroll = rememberScrollState()
    LaunchedEffect(serverError) { if (serverError != null) scroll.scrollTo(0) }
    Column(Modifier.fillMaxWidth()) {
        Column(
            Modifier.weight(1f, fill = false).sauceVerticalScroll(scroll)
                .padding(horizontal = SauceSpacing.large, vertical = SauceSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
        ) {
            serverError?.let { InlineRegistrationError(it) }
            if (entry == null) SheetText("Сохраните старт, на который зарегистрировались на другом сайте.")
            RegistrationPanel("Информация о старте") {
                SauceTextField("Название старта *", startName, { startName = it }, position(ExternalRegistrationField.StartName),
                    enabled = !busy, errorMessage = fieldError(ExternalRegistrationField.StartName),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, capitalization = KeyboardCapitalization.Sentences))
                SauceTextField("Дистанция *", distanceName, { distanceName = it }, position(ExternalRegistrationField.DistanceName),
                    enabled = !busy, placeholder = "Например, 10 км", errorMessage = fieldError(ExternalRegistrationField.DistanceName),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next))
                RegistrationDateField(date, !busy, position(ExternalRegistrationField.Date), external = true,
                    error = fieldError(ExternalRegistrationField.Date), onChange = { date = it })
                SauceTextField("Ссылка на страницу старта *", url, { url = it }, position(ExternalRegistrationField.Url),
                    enabled = !busy, placeholder = "https://", errorMessage = fieldError(ExternalRegistrationField.Url),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, imeAction = ImeAction.Next))
            }
            RegistrationPanel("Данные участника") {
                SauceTextField("Возрастная группа", ageGroup, { ageGroup = it }, Modifier.fillMaxWidth(),
                    enabled = !busy, placeholder = "Необязательно", keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next))
                SauceTextField("Команда", team, { team = it }, Modifier.fillMaxWidth(),
                    enabled = !busy, placeholder = "Необязательно", keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done))
            }
            RegistrationPanel("Постер") {
                val preview = poster?.bytes ?: entry?.poster?.url?.takeIf { showExistingPoster }
                if (preview != null) {
                    AsyncImage(preview, "Предпросмотр постера",
                        Modifier.fillMaxWidth().height(144.dp).clip(SauceShapes.field).background(SauceTheme.colors.surfaceSubtle),
                        contentScale = ContentScale.Fit)
                }
                poster?.name?.let { SheetText(it) }
                SheetText("JPG, PNG или WebP до 5 МБ")
                if (readingPoster) LinearProgressIndicator(Modifier.fillMaxWidth())
                fileError?.let {
                    InlineRegistrationError(it)
                    SauceButton("Не использовать этот файл", variant = SauceButtonVariant.Text, onClick = { fileError = null })
                }
                val hasPoster = poster != null || posterId != null || showExistingPoster
                SauceButton(if (hasPoster) "Заменить постер" else "Выбрать постер",
                    enabled = !busy && !readingPoster, variant = SauceButtonVariant.Outlined, onClick = pick)
                if (hasPoster) SauceButton("Убрать постер", enabled = !busy && !readingPoster, variant = SauceButtonVariant.Text, onClick = {
                    posterId = null
                    showExistingPoster = false
                    poster = null
                    fileError = null
                })
            }
        }
        Surface(color = SauceTheme.colors.surface) {
            SauceButton(
                if (entry == null) "Добавить регистрацию" else "Сохранить изменения",
                modifier = Modifier.padding(horizontal = SauceSpacing.large, vertical = SauceSpacing.medium),
                enabled = !readingPoster && fileError == null,
                loading = busy,
                onClick = {
                    focus.clearFocus()
                    if (errors.isEmpty()) submit(Command.SaveExternal(draft))
                    else validationAttempt++
                },
            )
        }
    }
}
