package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.*
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.*

@Composable
internal fun StartContentBasePanel(modifier: Modifier = Modifier, label: String, icon: ImageVector? = null,
    onHeaderClick: (() -> Unit)? = null, expanded: Boolean = false, content: @Composable () -> Unit) {
    OnBackgroundCard(modifier) {
        Column(Modifier.fillMaxWidth().padding(SauceSpacing.large), verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
            StartSectionHeading(label, icon, onClick = onHeaderClick, expanded = expanded)
            content()
        }
    }
}

@Composable
internal fun StartSectionHeading(title: String, icon: ImageVector? = null, modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null, expanded: Boolean = false) {
    val row: @Composable () -> Unit = {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
            if (icon != null) Icon(icon, null, Modifier.size(SauceSizes.icon), tint = SauceTheme.colors.textSecondary)
            Text(title, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, color = SauceTheme.colors.textPrimary)
            if (onClick != null) Icon(if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                null, Modifier.size(SauceSizes.icon), tint = SauceTheme.colors.textMuted)
        }
    }
    if (onClick == null) Box(modifier) { row() }
    else Surface(modifier = modifier.fillMaxWidth().heightIn(min = SauceSizes.touchTarget).semantics {
        stateDescription = if (expanded) "Развёрнуто" else "Свёрнуто"
    }, onClick = onClick, shape = SauceShapes.field, color = SauceTheme.colors.surface) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { row() }
    }
}
