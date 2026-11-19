package com.markettwits.sportsouce.start.presentation.membres.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.divider.BaseDivider
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.presentation.common.components.*
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi

@Composable
internal fun StartMemberCard(item: StartMembersUi, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier.fillMaxWidth()) {
        val wide = maxWidth >= 700.dp * LocalDensity.current.fontScale
        val identity = when (item) {
            is StartMembersUi.Single -> "single:${item.id}"
            is StartMembersUi.Team -> "team:${item.team}:${item.members.map { it.memberId }}:${item.distance}"
        }
        var expanded by rememberSaveable(identity) { mutableStateOf(false) }
        val details = listOf("Команда" to (if (item is StartMembersUi.Team) "" else item.team), "Группа" to item.group, "Город" to item.city)
            .filter { it.second.isNotBlank() }
        StartListCard(
            modifier = Modifier.fillMaxWidth().semantics {
                if (!wide) stateDescription = if (expanded) "Подробности раскрыты" else "Подробности свёрнуты"
            },
            onClick = if (wide) null else ({ expanded = !expanded }),
        ) {
            Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        when (item) {
                            is StartMembersUi.Single -> Text("${item.surname} ${item.name}".trim(), Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
                            is StartMembersUi.Team -> {
                                Text(item.team.ifBlank { "Команда" }, Modifier.fillMaxWidth(),
                                    style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
                                item.members.forEach { member ->
                                    Text("${member.surname} ${member.name}".trim(), Modifier.fillMaxWidth(),
                                        style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
                                }
                            }
                        }
                    }
                    if (!wide) Icon(if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null, tint = SauceTheme.colors.textMuted, modifier = Modifier.size(24.dp))
                }
                StartListTag(item.distance)
            }
            if ((wide || expanded) && details.isNotEmpty()) {
                BaseDivider()
                Surface(color = SauceTheme.colors.surfaceSubtle) {
                    StartListFacts(
                        details,
                        modifier = Modifier.padding(12.dp),
                    )
                }
            }
        }
    }
}
