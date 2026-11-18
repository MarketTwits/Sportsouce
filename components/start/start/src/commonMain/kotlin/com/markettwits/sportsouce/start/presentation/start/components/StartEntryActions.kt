package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSizes
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun StartEntryActions(
    modifier: Modifier = Modifier,
    membersCount: Int,
    resultsCount: Int,
    onClickMembers: () -> Unit,
    onClickResults: () -> Unit,
) {
    if (membersCount == 0 && resultsCount == 0) return
    BoxWithConstraints(modifier.fillMaxWidth()) {
        val spacing = Arrangement.spacedBy(SauceSpacing.small)
        val actions: @Composable (Modifier) -> Unit = { itemModifier ->
            if (membersCount > 0) {
                StartEntryAction(itemModifier, "Участники", membersCount, Icons.Outlined.Group, onClickMembers)
            }
            if (resultsCount > 0) {
                StartEntryAction(itemModifier, "Результаты", resultsCount, Icons.Outlined.EmojiEvents, onClickResults)
            }
        }
        if (membersCount > 0 && resultsCount > 0 && maxWidth >= 300.dp * LocalDensity.current.fontScale) {
            Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = spacing) {
                actions(Modifier.weight(1f).fillMaxHeight())
            }
        } else {
            Column(Modifier.fillMaxWidth(), verticalArrangement = spacing) { actions(Modifier.fillMaxWidth()) }
        }
    }
}

@Composable
private fun StartEntryAction(modifier: Modifier, title: String, count: Int, icon: ImageVector, onClick: () -> Unit) {
    val colors = SauceTheme.colors
    Surface(
        onClick = onClick,
        modifier = modifier.heightIn(min = SauceSizes.touchTarget),
        shape = SauceShapes.field,
        color = colors.actionSoft,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = SauceSpacing.medium, vertical = SauceSpacing.small),
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small),
            ) {
                Icon(icon, null, Modifier.size(SauceSizes.icon), tint = colors.action)
                Text(count.toString(), Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, color = colors.textPrimary)
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, Modifier.size(SauceSizes.icon), tint = colors.action)
            }
            Text(title, Modifier.fillMaxWidth(), style = MaterialTheme.typography.labelLarge, color = colors.textSecondary)
        }
    }
}
