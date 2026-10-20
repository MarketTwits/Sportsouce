package com.markettwits.sportsouce.club.dashboard.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun SubscriptionInfoCard(onClick: () -> Unit, onTrialClick: () -> Unit) {
    OnBackgroundCard {
        Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Первая тренировка — бесплатно", style = MaterialTheme.typography.titleLarge, color = SauceTheme.colors.textPrimary)
            Text("Познакомьтесь с тренером и выберите свою группу.",
                style = MaterialTheme.typography.bodyMedium, color = SauceTheme.colors.textSecondary)
            SauceButton("Выбрать тренировку", onTrialClick)
            SauceButton("Абонементы и цены", onClick, variant = SauceButtonVariant.Text)
        }
    }
}
