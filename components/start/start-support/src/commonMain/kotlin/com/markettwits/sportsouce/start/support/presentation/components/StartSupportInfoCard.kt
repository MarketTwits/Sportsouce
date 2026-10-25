package com.markettwits.sportsouce.start.support.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme


@Composable
internal fun StartSupportInfoCard(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = SauceShapes.field,
        color = SauceTheme.colors.surfaceSubtle,
    ) {
        BoxWithConstraints(Modifier.padding(16.dp)) {
            val showAnimation = maxWidth >= 300.dp && LocalDensity.current.fontScale <= 1.3f
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Добровольная поддержка — не оплата участия.\n\n" +
                            "Пожертвование помогает развивать спортивные старты и делать их доступнее.",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                    color = SauceTheme.colors.textSecondary,
                )
                if (showAnimation) {
                    StartSupportAnimation(Modifier.width(64.dp))
                }
            }
        }
    }
}
