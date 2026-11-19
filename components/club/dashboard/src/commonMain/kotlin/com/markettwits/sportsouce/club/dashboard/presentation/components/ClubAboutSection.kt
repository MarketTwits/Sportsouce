package com.markettwits.sportsouce.club.dashboard.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun ClubAboutSection(
    onPlanClick: () -> Unit,
    onClubBonusesClick: () -> Unit,
    onFaqClick: () -> Unit,
    questionCount: Int,
    onStatisticsClick: (() -> Unit)?,
) {
    val colors = SauceTheme.colors
    val rowPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("О клубе", Modifier.semantics { heading() }, style = MaterialTheme.typography.headlineSmall, color = colors.textPrimary)
        OnBackgroundCard {
            Column(Modifier.fillMaxWidth().padding(6.dp)) {
                SauceActionRow("Как начать", onPlanClick, icon = Icons.Outlined.Flag, contentPadding = rowPadding)
                HorizontalDivider(Modifier.padding(horizontal = 12.dp), color = colors.border)
                SauceActionRow("Возможности клуба", onClubBonusesClick, icon = Icons.Outlined.StarOutline, contentPadding = rowPadding)
                if (onStatisticsClick != null) {
                    HorizontalDivider(Modifier.padding(horizontal = 12.dp), color = colors.border)
                    SauceActionRow("Клуб в цифрах", onStatisticsClick, icon = Icons.AutoMirrored.Filled.ShowChart, contentPadding = rowPadding)
                }
            }
        }
        OnBackgroundCard {
            Column(Modifier.fillMaxWidth().padding(6.dp)) {
                SauceActionRow(
                    title = "Вопросы и ответы",
                    onClick = onFaqClick,
                    subtitle = "О тренировках и записи в клуб",
                    icon = Icons.AutoMirrored.Filled.HelpOutline,
                    count = questionCount.takeIf { it > 0 }?.toString(),
                    contentPadding = rowPadding,
                )
            }
        }
    }
}
