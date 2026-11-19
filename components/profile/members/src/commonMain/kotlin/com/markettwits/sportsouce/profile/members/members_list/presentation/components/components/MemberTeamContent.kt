package com.markettwits.sportsouce.profile.members.members_list.presentation.components.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.*
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.components.status.*
import com.markettwits.core_ui.items.components.selection.SauceSwitchRow
import com.markettwits.core_ui.items.components.textField.ItemsTextFiledDialog
import com.markettwits.core_ui.items.screens.PullToRefreshScreen
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.members.member_common.domain.*
import com.markettwits.sportsouce.profile.members.members_list.presentation.store.store.MembersListStore

@Composable
internal fun MemberTeamContent(state: MembersListStore.State, requests: Boolean, onRefresh: () -> Unit,
    onIntent: (MembersListStore.Intent) -> Unit) {
    PullToRefreshScreen(modifier = Modifier.fillMaxSize(), isRefreshing = state.teamLoading && state.team != null, onRefresh = onRefresh) {
        SauceLazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(SauceSpacing.large),
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
            if (state.teamLoading && state.team == null) item {
                Column(Modifier.semantics { contentDescription = "Загрузка связей"; progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate },
                    verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
                    repeat(3) { SauceSkeleton(Modifier.fillMaxWidth().height(120.dp)) }
                }
            }
            state.teamError?.let { error -> item {
                TeamError(error, onRefresh)
            } }
            state.team?.let { team ->
                if (requests) {
                    if (team.incoming.isEmpty() && team.outgoing.isEmpty()) item { TeamEmpty("Запросов пока нет") }
                    if (team.incoming.isNotEmpty()) {
                        item { Text("Входящие", style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary) }
                        team.incoming.forEach { request -> item(key = "incoming-${request.id}") { TeamRequestCard(request, incoming = true, busy = state.actingTeamKey != null || state.teamLoading,
                            onRespond = { onIntent(MembersListStore.Intent.RespondToRequest(request.id, it)) }) } }
                    }
                    if (team.outgoing.isNotEmpty()) {
                        item { Text("Исходящие", style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary) }
                        team.outgoing.forEach { request -> item(key = "outgoing-${request.id}") { TeamRequestCard(request, incoming = false, busy = state.actingTeamKey != null || state.teamLoading, onRespond = {}) } }
                    }
                } else {
                    item { Text("Кто может регистрировать меня", style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary) }
                    if (team.trusted.isEmpty()) item { TeamEmpty("Вы пока никому не предоставили доступ") }
                    team.trusted.forEach { person -> item(key = "trusted-${person.id}") { MemberAccessCard(person, trusted = true, busy = state.actingTeamKey != null || state.teamLoading,
                        onRelationChanged = { onIntent(MembersListStore.Intent.UpdateRelation(person.userId, it)) },
                        onAutoAcceptChanged = { onIntent(MembersListStore.Intent.UpdateAutoAccept(person.id, it)) }) } }
                }
            }
        }
    }
}

@Composable
internal fun MemberAccessCard(
    access: MemberTeamAccess,
    trusted: Boolean,
    busy: Boolean = false,
    canAddTrust: Boolean = false,
    onRelationChanged: ((String?) -> Unit)? = null,
    onAutoAcceptChanged: ((Boolean) -> Unit)? = null,
    onAddTrust: (() -> Unit)? = null,
) {
    OnBackgroundCard {
        Column(Modifier.fillMaxWidth().padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
            Text(access.name, style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
            relationLabel(access.relation)?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted) }
            SauceStatusBadge("Связь подтверждена", tone = SauceStatusTone.Success)
            Text(if (trusted) "Может регистрировать вас на старты" else "Можете регистрировать на старты",
                style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
            onRelationChanged?.let { change ->
                val none = "Не указано"
                ItemsTextFiledDialog(label = "Кем вам приходится", value = memberRelationOptions[access.relation] ?: none,
                    items = listOf(none) + memberRelationOptions.values, enabled = !busy,
                    onValueChanged = { label -> change(memberRelationOptions.entries.firstOrNull { it.value == label }?.key) })
            }
            if (trusted && onAutoAcceptChanged != null) SauceSwitchRow(
                label = "Автоподтверждение регистраций", checked = access.autoAccept,
                supportingText = "Регистрации от этого человека будут приниматься без отдельного подтверждения.",
                enabled = !busy, onCheckedChange = onAutoAcceptChanged)
            if (canAddTrust && onAddTrust != null) {
                Text("Вы можете разрешить этому человеку регистрировать вас с автоматическим подтверждением.",
                    style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
                SauceButton("Разрешить автоподтверждение", onAddTrust, variant = SauceButtonVariant.Outlined, enabled = !busy)
            }
        }
    }
}

private fun relationLabel(value: String?): String? = when (value) {
    "relative" -> "Родственник"
    "friend" -> "Друг"
    "teammate" -> "Партнер по команде"
    "coach" -> "Тренер"
    "student" -> "Ученик"
    else -> null
}

@Composable
private fun TeamRequestCard(request: MemberConnectionRequest, incoming: Boolean, busy: Boolean, onRespond: (Boolean) -> Unit) {
    OnBackgroundCard {
        Column(Modifier.fillMaxWidth().padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
            Text(if (incoming) request.requesterName else request.targetName,
                style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
            Text(if (incoming) "Хочет регистрировать вас на старты" else "Вы запросили возможность регистрировать участника",
                style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
            val status = when (request.status) {
                "pending" -> "Ожидает подтверждения"
                "accepted" -> "Принят"
                "rejected", "declined" -> "Отклонён"
                "cancelled", "canceled" -> "Отменён"
                else -> "Статус уточняется"
            }
            SauceStatusBadge(status, tone = when (request.status) {
                "accepted" -> SauceStatusTone.Success
                "pending" -> SauceStatusTone.Warning
                else -> SauceStatusTone.Neutral
            })
            if (incoming && request.status == "pending") {
                SauceButton("Принять запрос", onClick = { onRespond(true) }, enabled = !busy)
                SauceButton("Отклонить", onClick = { onRespond(false) }, variant = SauceButtonVariant.Outlined, enabled = !busy)
            }
        }
    }
}

@Composable
internal fun TeamError(message: String, onRetry: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
        Text(message, color = SauceTheme.colors.danger.content, style = MaterialTheme.typography.bodyMedium)
        SauceButton("Повторить", onRetry, variant = SauceButtonVariant.Outlined)
    }
}

@Composable
private fun TeamEmpty(title: String) {
    OnBackgroundCard {
        Text(title, Modifier.padding(SauceSpacing.large), color = SauceTheme.colors.textSecondary, style = MaterialTheme.typography.bodyMedium)
    }
}
