package com.markettwits.sportsouce.club.dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun SubscriptionInfoCard(onClick: () -> Unit, onTrialClick: () -> Unit) {
    val colors = SauceTheme.colors
    Surface(shape = SauceShapes.card, color = colors.actionSoft) {
        BoxWithConstraints(Modifier.fillMaxWidth().padding(20.dp)) {
            val actions: @Composable () -> Unit = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    SauceButton("Выбрать тренировку", onTrialClick, variant = SauceButtonVariant.Outlined)
                    SauceButton("Абонементы и цены", onClick, variant = SauceButtonVariant.Text)
                }
            }
            if (maxWidth >= 500.dp * LocalDensity.current.fontScale) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Column(Modifier.weight(1.2f)) { TrialIntroduction() }
                    Column(Modifier.weight(1f)) { actions() }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    TrialIntroduction()
                    actions()
                }
            }
        }
    }
}

@Composable
private fun TrialIntroduction() {
    val colors = SauceTheme.colors
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier.size(40.dp).background(colors.surface, SauceShapes.field),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Outlined.EventAvailable, null, Modifier.size(22.dp), tint = colors.action)
        }
        Text(
            "Первая тренировка бесплатно",
            Modifier.semantics { heading() },
            style = MaterialTheme.typography.titleLarge,
            color = colors.textPrimary,
            textAlign = TextAlign.Center,
        )
        Text(
            "Познакомьтесь с тренером и выберите свою группу.",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
        )
    }
}
