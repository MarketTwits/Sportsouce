package com.markettwits.sportsouce.profile.members.member_search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.*
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.components.status.*
import com.markettwits.core_ui.items.components.textField.OutlinedTextFieldBase
import com.markettwits.core_ui.items.components.topbar.*
import com.markettwits.core_ui.items.screens.AdaptivePane
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.members.member_common.domain.MemberCandidate
import com.markettwits.sportsouce.profile.members.member_common.domain.MemberConnectionRequest

@Composable
fun MemberSearchScreen(component: MemberSearchComponent) {
    val state by component.state.collectAsState()
    SauceScaffold(topBar = { TopBarWithClip(title = "Найти участника", goBack = component.back) }) { padding ->
        AdaptivePane(modifier = Modifier.fillMaxSize().padding(padding).imePadding()) {
            MemberSearchContent(state, component::updateName, component::updateSurname, component::updateYear,
                component::search, component::requestConnection, component.addManually, component::refreshConnections, component::updateProfileId)
        }
    }
}

@Composable
internal fun MemberSearchContent(
    state: MemberSearchState,
    onName: (String) -> Unit,
    onSurname: (String) -> Unit,
    onYear: (String) -> Unit,
    onSearch: () -> Unit,
    onRequest: (Int) -> Unit,
    onAddManually: () -> Unit,
    onRefreshConnections: () -> Unit,
    onProfileId: (String) -> Unit,
) {
    SauceLazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(SauceSpacing.large),
        verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
        item(key = "form") {
            OnBackgroundCard {
                Column(Modifier.fillMaxWidth().padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
                    Text("После подтверждения запроса вы сможете регистрировать человека на старты.",
                        style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
                    OutlinedTextFieldBase(label = "Фамилия", value = state.surname, isEnabled = state.sendingUserId == null,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), onValueChange = onSurname)
                    OutlinedTextFieldBase(label = "Имя", value = state.name, isEnabled = state.sendingUserId == null,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), onValueChange = onName)
                    OutlinedTextFieldBase(label = "Год рождения", value = state.birthYear, isEnabled = state.sendingUserId == null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done), onValueChange = onYear)
                    OutlinedTextFieldBase(label = "ID профиля (необязательно)", value = state.profileId, isEnabled = state.sendingUserId == null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done), onValueChange = onProfileId)
                    SauceButton("Найти", onSearch, loading = state.loading, enabled = state.sendingUserId == null)
                }
            }
        }
        state.error?.let { message -> item(key = "error") {
            Text(message, Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.danger.content)
        } }
        state.accessError?.let { message -> item(key = "access-error") {
            Column(verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                Text(message, color = SauceTheme.colors.danger.content, style = MaterialTheme.typography.bodyMedium)
                SauceButton("Обновить статусы связей", onRefreshConnections, variant = SauceButtonVariant.Outlined)
            }
        } }
        when {
            state.loading -> item(key = "loading") {
                Column(Modifier.semantics { contentDescription = "Поиск участников"; progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate },
                    verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                    repeat(2) { SauceSkeleton(Modifier.fillMaxWidth().height(128.dp)) }
                }
            }
            state.result != null -> {
                item(key = "result-label") {
                    Column(verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                        Text(state.result.message.ifBlank { if (state.result.candidates.isEmpty()) "Совпадений не найдено" else "Выберите участника" },
                            style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary,
                            modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
                        if (!state.accessReady && state.accessError == null) Text("Проверяем существующие связи…",
                            style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
                        state.result.clarificationHints.forEach { Text(it, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary) }
                    }
                }
                items(state.result.candidates, key = { "candidate-${it.userId}" }) { candidate ->
                    CandidateCard(candidate, state.requests[candidate.userId], state.sendingUserId, state.accessReady,
                        candidate.userId in state.connectedUserIds,
                        onRequest = { onRequest(candidate.userId) })
                }
            }
        }
        item(key = "manual") {
            SauceButton("Добавить без аккаунта", onAddManually, variant = SauceButtonVariant.Outlined,
                enabled = state.sendingUserId == null)
        }
    }
}

@Composable
private fun CandidateCard(candidate: MemberCandidate, request: MemberConnectionRequest?, sendingId: Int?,
    accessReady: Boolean, connected: Boolean, onRequest: () -> Unit) {
    OnBackgroundCard {
        Column(Modifier.fillMaxWidth().padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
            Text("${candidate.surname} ${candidate.name}".trim().ifBlank { "Профиль № ${candidate.userId}" }, style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
            Text(listOfNotNull(candidate.birthYear?.let { "Год рождения: $it" }, "ID ${candidate.userId}", candidate.city).joinToString(" · "),
                style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
            SauceStatusBadge(if (candidate.confidence == "HIGH") "Точное совпадение" else "Возможное совпадение",
                tone = if (candidate.confidence == "HIGH") SauceStatusTone.Neutral else SauceStatusTone.Warning, showIndicator = false)
            candidate.reasons.forEach { Text(it, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textSecondary) }
            when {
                connected || request?.status == "accepted" -> SauceStatusBadge("Связь подтверждена", tone = SauceStatusTone.Success)
                request != null -> {
                    SauceStatusBadge(if (request.status == "pending") "Ожидает подтверждения" else "Запрос создан", tone = SauceStatusTone.Warning)
                }
                candidate.canRequestConnection -> SauceButton("Отправить запрос", onRequest, variant = SauceButtonVariant.Soft,
                    loading = sendingId == candidate.userId, enabled = sendingId == null && accessReady)
                else -> Text(candidate.unavailableReason ?: "Запрос для этого профиля недоступен",
                    style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
            }
        }
    }
}
