package com.markettwits.sportsouce.profile.results.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.items.components.bottom_sheet.DefaultModalBottomSheet
import com.markettwits.core_ui.items.components.buttons.*
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.dialog.*
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.components.navigation.SauceLazyVerticalGrid
import com.markettwits.core_ui.items.components.status.*
import com.markettwits.core_ui.items.components.textField.*
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.results.domain.*
import com.markettwits.sportsouce.profile.results.presentation.actions.*
import com.markettwits.sportsouce.profile.results.presentation.actions.ResultsActionStore.Intent
import com.markettwits.sportsouce.profile.results.presentation.component.MyResultsComponent

@Composable
internal fun ResultsActionSlot(component: MyResultsComponent) {
    val slot by component.actionSlot.subscribeAsState()
    slot.child?.instance?.let { action ->
        val state by action.state.collectAsState()
        DefaultModalBottomSheet(onDismissRequest = { action.accept(Intent.Close) }, title = when (state.action) {
            ResultsAction.Find -> "Найти свой результат"; ResultsAction.Add -> "Добавить результат"; ResultsAction.Requests -> "Совпадения и запросы"
        }) {
            ResultsActionContent(state, action.categories, action::accept)
        }
        state.confirmation?.let { mutation ->
            SauceDialog(onDismissRequest = { action.accept(Intent.CancelConfirmation) }) {
                SauceDialogContent(title = if (mutation == ResultMutation.Discard) "Закрыть без сохранения?" else "Подтвердите действие",
                    actions = {
                        SauceButton(if (mutation == ResultMutation.Discard) "Закрыть" else "Подтвердить", { action.accept(Intent.SubmitConfirmation) })
                        SauceButton("Отмена", { action.accept(Intent.CancelConfirmation) }, variant = SauceButtonVariant.Text)
                    }) { Text(mutation.description(), color = SauceTheme.colors.textSecondary) }
            }
        }
    }
}

@Composable
internal fun ResultsActionContent(state: ResultsActionStore.State, categories: List<ResultCategory>, event: (Intent) -> Unit) {
    val grid = rememberLazyGridState()
    val focus = remember { ResultField.entries.associateWith { FocusRequester() } }
    LaunchedEffect(state.validationAttempt) {
        if (state.validationAttempt > 0) {
            val field = ResultField.entries.firstOrNull { it in state.errors }
            val custom = state.draft.category == "other"
            val index = when (field) {
                ResultField.Title -> 1; ResultField.Date -> 2; ResultField.Category -> 3
                ResultField.Distance -> 4; ResultField.Time -> if (custom) 5 else 4
                ResultField.Proof -> if (custom) 6 else 5; null -> null
            }
            if (index != null) {
                val prefix = listOf(state.error, state.loadError, state.success).count { it != null }
                grid.scrollToItem(index + prefix)
                withFrameNanos { }
                if (field in listOf(ResultField.Title, ResultField.Distance, ResultField.Time, ResultField.Proof)) focus[field]?.requestFocus()
            }
        }
    }
    val pick = rememberResultProofPicker { file, error -> event(Intent.File(file, error)) }
    SauceLazyVerticalGrid(GridCells.Adaptive(310.dp), Modifier.fillMaxWidth().fillMaxHeight(.9f).imePadding(), state = grid,
        contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        state.error?.let { error -> item("error", span = { GridItemSpan(maxLineSpan) }) { Text(error, color = SauceTheme.colors.danger.content) } }
        state.loadError?.let { error -> item("load-error", span = { GridItemSpan(maxLineSpan) }) { ResultsError(error) { event(Intent.Reload) } } }
        if (state.success != null) item("success", span = { GridItemSpan(maxLineSpan) }) {
            OnBackgroundCard { Text(state.success, Modifier.padding(16.dp), color = SauceTheme.colors.success.content) }
        }
        when (state.action) {
            ResultsAction.Add -> {
                if (state.success != null) item("done", span = { GridItemSpan(maxLineSpan) }) { SauceButton("Готово", { event(Intent.Close) }) }
                else {
                    item("add-intro", span = { GridItemSpan(maxLineSpan) }) { Text("Добавьте старт, которого нет на сайте. Для подтверждения приложите протокол или ссылку на результат.", style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary) }
                    item("title", span = { GridItemSpan(maxLineSpan) }) { ResultFormField("Название старта", state.draft.title, ResultField.Title, state, event, focusRequester = focus[ResultField.Title]) }
                    item("date") {
                        CalendarTextFiled(value = state.draft.date.split('-').reversed().joinToString("."), enabled = !state.busy,
                            textFiled = { modifier -> SauceTextField("Дата старта", resultDate(state.draft.date).takeIf { state.draft.date.isNotEmpty() }.orEmpty(), {}, modifier,
                                readOnly = true, enabled = !state.busy, errorMessage = state.errors[ResultField.Date]) },
                            onValueChanged = { event(Intent.Field(ResultField.Date, it.split('.').reversed().joinToString("-"))) })
                    }
                    item("category") {
                        val options = categories.map { it.key to it.label } + ("other" to "Другая дистанция (бег)")
                        ItemsTextFiledDialog(label = "Дистанция", value = options.find { it.first == state.draft.category }?.second.orEmpty(),
                            items = options.map { it.second }, enabled = !state.busy, onValueChanged = { text -> options.find { it.second == text }?.let { event(Intent.Field(ResultField.Category, it.first)) } })
                    }
                    if (state.draft.category == "other") item("distance") { ResultFormField("Длина, км", state.draft.distance, ResultField.Distance, state, event, KeyboardType.Decimal, focusRequester = focus[ResultField.Distance]) }
                    item("time") { ResultFormField("Результат", state.draft.time, ResultField.Time, state, event, KeyboardType.Text, "1:42:18", focus[ResultField.Time]) }
                    item("proof", span = { GridItemSpan(maxLineSpan) }) { ResultFormField("Ссылка на официальный результат", state.draft.proof, ResultField.Proof, state, event, KeyboardType.Uri, "https://…", focus[ResultField.Proof]) }
                    item("file", span = { GridItemSpan(maxLineSpan) }) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            SauceButton(state.file?.name ?: "Прикрепить протокол", pick, variant = SauceButtonVariant.Outlined, enabled = !state.busy)
                            Text("PDF или изображение, до 10 МБ", style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
                            state.fileError?.let { Text(it, color = SauceTheme.colors.danger.content, style = MaterialTheme.typography.bodySmall) }
                            if (state.file != null) SauceButton("Убрать файл", { event(Intent.File(null)) }, enabled = !state.busy, fillWidth = false, variant = SauceButtonVariant.Text)
                        }
                    }
                    item("save", span = { GridItemSpan(maxLineSpan) }) { SauceButton(if (state.busy) "Отправляем…" else "Отправить на проверку", { event(Intent.Save) }, enabled = !state.busy) }
                }
            }
            ResultsAction.Find -> {
                item("find-intro", span = { GridItemSpan(maxLineSpan) }) { Text("Найдите своё имя в протоколе или введите стартовый номер. Запрос на привязку будет виден в разделе «Совпадения и запросы».", style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary) }
                item("start-query", span = { GridItemSpan(maxLineSpan) }) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        SauceTextField("Старт", state.startQuery, { event(Intent.StartQuery(it)) }, enabled = !state.busy,
                            placeholder = "Название или ID старта", supportingText = state.startId?.let { "Выбран старт № $it" }, errorMessage = state.startError)
                        if (state.startsLoading) Text("Ищем старты…", style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary)
                    }
                }
                items(state.starts, key = { "start-${it.id}" }, span = { GridItemSpan(maxLineSpan) }) { start ->
                    OnBackgroundCard { SauceActionRow("${start.name} · № ${start.id}", { event(Intent.SelectStart(start)) }) }
                }
                item("name-query", span = { GridItemSpan(maxLineSpan) }) { SauceTextField("ФИО или стартовый номер", state.query, { event(Intent.Query(it)) }, enabled = !state.busy, placeholder = "Иванов Иван") }
                item("find", span = { GridItemSpan(maxLineSpan) }) { SauceButton(if (state.searching) "Ищем…" else "Найти", { event(Intent.Search) }, enabled = !state.searching && !state.busy) }
                if (state.searching) item("search-loading", span = { GridItemSpan(maxLineSpan) }) { ResultsSkeleton() }
                if (state.results?.isEmpty() == true) item("not-found", span = { GridItemSpan(maxLineSpan) }) { Text("Совпадений не найдено. Попробуйте другое имя или старт.", color = SauceTheme.colors.textSecondary) }
                items(state.results.orEmpty(), key = { "unmatched-${it.id}" }) { result ->
                    val requested = result.id in state.submittedResults || state.claims.any { it.result?.id == result.id && (it.type != "ADMIN_SUGGESTION" || it.status == "PENDING") }
                    OnBackgroundCard { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(result.name.ifBlank { "Участник" }, style = MaterialTheme.typography.titleSmall)
                        Text(listOfNotNull(result.birthday ?: result.birthYear?.let { "$it г." }, result.start?.name, result.distance, result.result).joinToString(" · "), style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary)
                        if (result.birthYearAnomaly) Text("В протоколе указан необычный год рождения. Проверьте данные.", style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.warning.content)
                        SauceButton(if (requested) "Запрос уже отправлен" else "Запросить привязку", { event(Intent.Confirm(ResultMutation.Claim(result))) },
                            enabled = !requested && !state.busy && !state.loading && state.loadError == null, variant = SauceButtonVariant.Outlined)
                    } }
                }
            }
            ResultsAction.Requests -> {
                if (state.loading) item("requests-loading", span = { GridItemSpan(maxLineSpan) }) { ResultsSkeleton() }
                item("matches-title", span = { GridItemSpan(maxLineSpan) }) { SectionTitle("Возможные совпадения") }
                if (!state.loading && state.candidates.isEmpty() && state.loadError == null) item("no-matches", span = { GridItemSpan(maxLineSpan) }) { Text("Новых совпадений пока нет.", color = SauceTheme.colors.textSecondary) }
                items(state.candidates, key = { "candidate-${it.id}" }) { candidate -> CandidateCard(candidate, state.busy, event) }
                if (state.dismissed.isNotEmpty()) item("dismissed-title", span = { GridItemSpan(maxLineSpan) }) { SectionTitle("Скрытые совпадения") }
                items(state.dismissed, key = { "dismissed-${it.id}" }) { candidate ->
                    OnBackgroundCard { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(candidate.title(), style = MaterialTheme.typography.titleSmall)
                        SauceButton("Вернуть в совпадения", { event(Intent.Confirm(ResultMutation.Candidate(candidate, "restore"))) }, enabled = !state.busy, variant = SauceButtonVariant.Outlined)
                    } }
                }
                if (state.suggestions.isNotEmpty()) item("suggestions-title", span = { GridItemSpan(maxLineSpan) }) { SectionTitle("Предложения администратора") }
                items(state.suggestions, key = { "suggestion-${it.id}" }) { request -> RequestCard(request, true, state.busy, event) }
                item("claims-title", span = { GridItemSpan(maxLineSpan) }) { SectionTitle("Мои запросы") }
                if (!state.loading && state.claims.isEmpty() && state.loadError == null) item("no-claims", span = { GridItemSpan(maxLineSpan) }) { Text("Запросов пока нет. Найдите свой результат в протоколе, чтобы запросить привязку.", color = SauceTheme.colors.textSecondary) }
                items(state.claims, key = { "claim-${it.id}" }) { request -> RequestCard(request, false, state.busy, event) }
            }
        }
    }
}

@Composable
private fun ResultFormField(label: String, value: String, field: ResultField, state: ResultsActionStore.State, event: (Intent) -> Unit,
    keyboard: KeyboardType = KeyboardType.Text, placeholder: String? = null, focusRequester: FocusRequester? = null) {
    SauceTextField(label, value, { event(Intent.Field(field, it)) }, modifier = if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier, enabled = !state.busy, errorMessage = state.errors[field],
        keyboardOptions = KeyboardOptions(keyboardType = keyboard), placeholder = placeholder)
}

@Composable
private fun CandidateCard(candidate: ResultCandidate, busy: Boolean, event: (Intent) -> Unit) {
    OnBackgroundCard { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(candidate.title(), style = MaterialTheme.typography.titleSmall)
        candidate.targetResult?.let { Text(listOfNotNull(it.start?.name, it.distance, it.result).joinToString(" · "), style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary) }
        candidate.targetUser?.birthday?.let { Text(resultDate(it), style = MaterialTheme.typography.bodySmall) }
        candidate.displayScore?.let { Text("Совпадение: ${it.toInt()}%", style = MaterialTheme.typography.labelMedium, color = SauceTheme.colors.action) }
        candidate.reasons.mapNotNull { it.label }.filter { it.isNotBlank() }.forEach { Text(it, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary) }
        candidate.baseAgeGroupDisclaimer?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.warning.content) }
        SauceButton("Это я", { event(Intent.Confirm(ResultMutation.Candidate(candidate, "accept"))) }, enabled = !busy)
        SauceButton("Скрыть", { event(Intent.Confirm(ResultMutation.Candidate(candidate, "dismiss"))) }, enabled = !busy, variant = SauceButtonVariant.Text)
    } }
}

@Composable
private fun RequestCard(request: ResultClaim, suggestion: Boolean, busy: Boolean, event: (Intent) -> Unit) {
    OnBackgroundCard { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SauceStatusBadge(requestStatus(request.status), tone = when (request.status) { "ACCEPTED", "CONFIRMED" -> SauceStatusTone.Success; "PENDING", "ADMIN_REVIEW" -> SauceStatusTone.Warning; else -> SauceStatusTone.Neutral })
        Text(request.result?.name ?: request.title ?: "Запрос на привязку", style = MaterialTheme.typography.titleSmall)
        request.result?.start?.name?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary) }
        request.comment?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary) }
        request.createdAt?.let { Text(resultDate(it), style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted) }
        if (request.status == "PENDING" && suggestion) {
            SauceButton("Подтвердить", { event(Intent.Confirm(ResultMutation.Suggestion(request, true))) }, enabled = !busy)
            SauceButton("Отказаться", { event(Intent.Confirm(ResultMutation.Suggestion(request, false))) }, enabled = !busy, variant = SauceButtonVariant.Text)
        }
        if (request.status == "PENDING" && request.type == "OWNERSHIP_CONFLICT") {
            SauceButton("Это мой результат", { event(Intent.Confirm(ResultMutation.Conflict(request, false))) }, enabled = !busy)
            SauceButton("Передать заявителю", { event(Intent.Confirm(ResultMutation.Conflict(request, true))) }, enabled = !busy, variant = SauceButtonVariant.Outlined)
        }
        if (request.type == "USER_CLAIM" && request.status in listOf("REJECTED", "CANCELED", "EXPIRED") && request.result?.id != null)
            SauceButton("Повторить заявку", { event(Intent.Confirm(ResultMutation.Reopen(request))) }, enabled = !busy, variant = SauceButtonVariant.Outlined)
    } }
}
private fun ResultCandidate.title() = targetUser?.let { listOfNotNull(it.surname, it.name).joinToString(" ") } ?: targetResult?.name ?: "Совпадение"
private fun requestStatus(status: String) = when (status) {
    "PENDING" -> "Ожидает"; "ACCEPTED" -> "Принято"; "CONFIRMED" -> "Подтверждено"; "REJECTED" -> "Отклонено"
    "ADMIN_REVIEW" -> "На проверке"; "CANCELED" -> "Отменено"; "EXPIRED" -> "Истекло"; else -> status
}
private fun ResultMutation.description(): String = when (this) {
    is ResultMutation.Claim -> "Запросить привязку результата «${result.name}» к вашему профилю? Администратор или владелец получит запрос."
    is ResultMutation.Candidate -> when (action) { "accept" -> "Подтвердить, что «${candidate.title()}» — это вы? Профиль или результат будет предложен к объединению или привязке."; "dismiss" -> "Скрыть это совпадение? Позже его можно вернуть."; else -> "Вернуть это совпадение в список?" }
    is ResultMutation.Suggestion -> if (accept) "Привязать результат к вашему профилю?" else "Отказаться от предложения администратора?"
    is ResultMutation.Conflict -> if (transfer) "Передать результат другому пользователю? Он перестанет принадлежать вашему профилю." else "Подтвердить, что этот результат принадлежит вам?"
    is ResultMutation.Reopen -> "Отправить заявку повторно? Администратор снова сможет принять решение."
    ResultMutation.Discard -> "Введённые данные не будут сохранены."
}
