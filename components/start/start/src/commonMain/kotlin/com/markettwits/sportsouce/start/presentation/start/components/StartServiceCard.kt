package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun StartServiceCard(
    title: String,
    description: String,
    action: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val colors = SauceTheme.colors
    OnBackgroundCard(modifier) {
        Surface(onClick = onClick, shape = SauceShapes.card, color = colors.surface) {
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.size(40.dp).background(colors.actionSoft, SauceShapes.field),
                    contentAlignment = Alignment.Center) {
                    Icon(icon, null, Modifier.size(24.dp), tint = colors.action)
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(title, Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleMedium, color = colors.textPrimary)
                    Text(description, Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary)
                    Row(Modifier.padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(action, Modifier.weight(1f), style = MaterialTheme.typography.labelLarge, color = colors.action)
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, null, Modifier.size(18.dp), tint = colors.action)
                    }
                }
            }
        }
        content?.invoke(this)
    }
}
