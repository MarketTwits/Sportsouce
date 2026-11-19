package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.components.status.SauceStatusBadge
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.auth.service.api.SharedUser
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderMember
import com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start.MemberResultsCard
import com.markettwits.sportsouce.profile.registrations.presentation.management.InlineRegistrationError
import com.markettwits.sportsouce.profile.registrations.presentation.management.displayRegistrationDate
import com.markettwits.sportsouce.profile.registrations.presentation.management.isCurrentUser
import com.markettwits.sportsouce.profile.registrations.presentation.management.isRegistrationPast

@Composable
internal fun OrderMembersTab(
    order: StartOrderInfo,
    user: SharedUser?,
    onCorrect: ((StartOrderMember) -> Unit)?,
    onDistance: (() -> Unit)?,
    transfersError: String?,
    onRetryTransfers: (() -> Unit)?,
    slotActions: (@Composable (StartOrderMember) -> Unit)?,
) {
    val past = order.isRegistrationPast()
    OrderDetailPage {
        if (past || order.isRefunded) RegistrationStatusBanner(order, archived = past)
        if (transfersError != null) {
            Column(verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                InlineRegistrationError(transfersError, title = "Не удалось проверить передачу слота")
                if (onRetryTransfers != null) {
                    SauceActionRow("Повторить проверку", onRetryTransfers, icon = Icons.Outlined.Sync)
                }
            }
        }
        if (order.members.isEmpty()) {
            OrderSectionCard {
                Text("Данных об участниках пока нет.", Modifier.padding(SauceSpacing.large),
                    style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
            }
        }
        order.members.forEachIndexed { index, member ->
            key(member.id ?: index) {
                RegistrationMemberCard(
                    member = member,
                    currentUser = member.isCurrentUser(user),
                    showOpenSlots = !past && !order.isRefunded,
                    onCorrect = member.id?.let { onCorrect?.let { action -> { action(member) } } },
                    onDistance = onDistance.takeIf { member.id != null },
                    slotActions = if (member.id != null && slotActions != null) ({ slotActions(member) }) else null,
                )
            }
        }
    }
}

@Composable
private fun RegistrationMemberCard(
    member: StartOrderMember,
    currentUser: Boolean,
    showOpenSlots: Boolean,
    onCorrect: (() -> Unit)?,
    onDistance: (() -> Unit)?,
    slotActions: (@Composable () -> Unit)?,
) {
    val colors = SauceTheme.colors
    var resultsExpanded by rememberSaveable { mutableStateOf(false) }
    val personalDetails = listOfNotNull(
        member.genderName.takeUnless { it.isBlank() || it == "N/A" },
        member.birthday?.take(10)?.takeIf(String::isNotBlank)?.let(::displayRegistrationDate),
    )
    val fields = listOf(
        "Дистанция" to member.distanceName,
        "Группа" to member.ageGroupName,
        "Команда" to member.teamName,
        "Формат" to member.formatName,
    ).filter { (_, value) -> value.isNotBlank() && value != "N/A" }

    OrderSectionCard {
        Column(Modifier.padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
            if (currentUser) SauceStatusBadge("Это вы", showIndicator = false)
            OrderSectionHeading("${member.surname} ${member.name}".trim())
            if (personalDetails.isNotEmpty()) Text(personalDetails.joinToString(" · "),
                style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary)
        }
        if (fields.isNotEmpty() || showOpenSlots && (member.infiniteSlots || member.openSlots != null)) {
            OrderSectionDivider()
            Column(Modifier.padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                fields.forEach { (label, value) -> OrderInfoLine(label, value) }
                if (showOpenSlots && (member.infiniteSlots || member.openSlots != null)) {
                    OrderInfoLine("Свободные слоты", if (member.infiniteSlots) "Не ограничены" else member.openSlots.toString())
                }
            }
        }
        val actionPadding = PaddingValues(horizontal = SauceSpacing.large, vertical = SauceSpacing.medium)
        if (member.results.isNotEmpty()) {
            OrderSectionDivider()
            SauceActionRow("Результаты", { resultsExpanded = !resultsExpanded },
                modifier = Modifier.semantics { stateDescription = if (resultsExpanded) "Развёрнуто" else "Свёрнуто" },
                icon = Icons.Outlined.EmojiEvents, contentPadding = actionPadding,
                trailingIcon = if (resultsExpanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore)
            AnimatedVisibility(resultsExpanded) {
                Column(Modifier.padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.large)) {
                    member.results.forEach { MemberResultsCard(it) }
                }
            }
        }
        if (onCorrect != null) {
            OrderSectionDivider()
            SauceActionRow("Исправить опечатку", onCorrect, icon = Icons.Outlined.Edit, contentPadding = actionPadding)
        }
        if (onDistance != null) {
            OrderSectionDivider()
            SauceActionRow("Изменить дистанцию", onDistance, icon = Icons.Outlined.SwapHoriz, contentPadding = actionPadding)
        }
        slotActions?.invoke()
    }
}
