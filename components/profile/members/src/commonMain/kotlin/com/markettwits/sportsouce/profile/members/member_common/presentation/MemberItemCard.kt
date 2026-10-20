package com.markettwits.sportsouce.profile.members.member_common.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.status.SauceStatusBadge
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.profile.members.member_common.domain.ProfileMember

@Composable
fun MemberItemCard(
    modifier: Modifier = Modifier,
    full: Boolean = false,
    item: ProfileMember,
    onClick: (ProfileMember) -> Unit,
    connectionConfirmed: Boolean = false,
) {
    OnBackgroundCard(modifier = modifier, onClick = if (full) null else ({ onClick(item) })) {
        Column(Modifier.fillMaxWidth().padding(SauceSpacing.large),
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
            Row(horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium), verticalAlignment = Alignment.Top) {
                MemberAvatar(item)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = listOf(item.surname, item.name).filter { it.isNotBlank() }.joinToString(" ").ifBlank { "Участник" },
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        color = SauceTheme.colors.textPrimary,
                        maxLines = if (full) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (item.type.isNotBlank()) Text(item.type,
                        style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
                }
                if (!full) Icon(Icons.Default.ChevronRight, contentDescription = null,
                    tint = SauceTheme.colors.textMuted, modifier = Modifier.size(20.dp))
            }
            if (item.identityKnown) {
                com.markettwits.core_ui.items.components.status.SauceStatusBadge(
                    label = when {
                        connectionConfirmed -> "Связь подтверждена"
                        item.participantUserId != null -> "С аккаунтом"
                        else -> "Без аккаунта"
                    },
                    tone = if (connectionConfirmed) com.markettwits.core_ui.items.components.status.SauceStatusTone.Success
                        else com.markettwits.core_ui.items.components.status.SauceStatusTone.Neutral,
                    showIndicator = connectionConfirmed,
                )
            }
            if (full) {
                Column(verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                    if (item.birthday.isNotBlank()) MemberDetailRow("Дата рождения", item.birthday)
                    if (item.gender.isNotBlank()) MemberDetailRow("Пол", item.gender)
                    if (item.team.isNotBlank()) MemberDetailRow("Команда", item.team)
                    if (item.phone.isNotBlank()) MemberDetailRow("Телефон", item.phone)
                    if (item.email.isNotBlank()) MemberDetailRow("Email", item.email)
                    if (!item.vk.isNullOrBlank()) MemberDetailRow("ВКонтакте", item.vk)
                    if (!item.telegram.isNullOrBlank()) MemberDetailRow("Telegram", item.telegram)
                    if (!item.whatsapp.isNullOrBlank()) MemberDetailRow("WhatsApp", item.whatsapp)
                    if (!item.instagram.isNullOrBlank()) MemberDetailRow("Instagram", item.instagram)
                    MemberDetailRow("Участник", if (item.child) "Ребёнок" else "Взрослый")
                }
            } else {
                if (item.birthday.isNotBlank() || item.child) FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small),
                    verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall),
                    itemVerticalAlignment = Alignment.CenterVertically,
                ) {
                    if (item.birthday.isNotBlank()) MemberContact(Icons.Default.Cake, item.birthday)
                    if (item.child) SauceStatusBadge("Ребёнок", showIndicator = false)
                }
                if (item.team.isNotBlank() || item.phone.isNotBlank() || item.email.isNotBlank()) {
                    Column(verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall)) {
                        if (item.team.isNotBlank()) MemberContact(Icons.Default.Groups, item.team)
                        if (item.phone.isNotBlank()) MemberContact(Icons.Default.Phone, item.phone)
                        if (item.email.isNotBlank()) MemberContact(Icons.Default.Email, item.email)
                    }
                }
            }
        }
    }
}

@Composable
private fun MemberAvatar(item: ProfileMember) {
    val initials = listOf(item.name, item.surname).mapNotNull { it.trim().firstOrNull()?.uppercase() }.joinToString("")
    Box(Modifier.sizeIn(minWidth = 40.dp, minHeight = 40.dp).clip(CircleShape)
        .background(SauceTheme.colors.surfaceSubtle).padding(8.dp)
        .clearAndSetSemantics {}, contentAlignment = Alignment.Center) {
        Text(initials, style = MaterialTheme.typography.labelLarge, color = SauceTheme.colors.textSecondary)
    }
}

@Composable
private fun MemberContact(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
        Icon(icon, contentDescription = null, tint = SauceTheme.colors.textMuted, modifier = Modifier.size(16.dp))
        Text(text, modifier = Modifier.weight(1f, fill = false), style = MaterialTheme.typography.bodySmall,
            color = SauceTheme.colors.textSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun MemberDetailRow(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textPrimary)
    }
}
