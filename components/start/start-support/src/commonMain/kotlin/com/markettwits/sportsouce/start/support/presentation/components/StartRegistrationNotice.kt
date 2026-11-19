package com.markettwits.sportsouce.start.support.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.support.domain.StartDonationCheck

@Composable
fun StartRegistrationNotice(check: StartDonationCheck, isUpcoming: Boolean, modifier: Modifier = Modifier) {
    val registration = check.registration
    val unpaid = check.hasUnpaidRegistration
    val amount = registration?.amountKopecks?.takeIf { it > 0 }?.let(::formatDonationAmount)
    val label = when {
        unpaid && amount != null -> "Регистрация не оплачена — $amount"
        unpaid -> "Регистрация не оплачена"
        isUpcoming -> "Слот зарезервирован"
        else -> "Вы зарегистрированы на старт"
    }
    val colors = SauceTheme.colors
    val tone = when {
        unpaid -> colors.warning
        isUpcoming -> colors.success
        else -> colors.neutral
    }
    Text(
        text = label,
        modifier = modifier.fillMaxWidth().background(tone.container)
            .padding(horizontal = SauceSpacing.large, vertical = SauceSpacing.medium)
            .semantics { liveRegion = LiveRegionMode.Polite },
        style = MaterialTheme.typography.labelMedium,
        color = tone.content,
        textAlign = TextAlign.Center,
    )
}
