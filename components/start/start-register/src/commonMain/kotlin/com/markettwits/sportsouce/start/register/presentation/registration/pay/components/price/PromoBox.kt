package com.markettwits.sportsouce.start.register.presentation.registration.pay.components.price

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
fun PromoBox(modifier: Modifier = Modifier, onClick: () -> Unit) {
    OnBackgroundCard(modifier = modifier, onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp).padding(SauceSpacing.large),
            horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Default.LocalOffer, null, tint = SauceTheme.colors.textSecondary)
            Text("Промокод", modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge, color = SauceTheme.colors.textPrimary)
            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, null,
                modifier = Modifier.size(20.dp), tint = SauceTheme.colors.textMuted)
        }
    }
}
