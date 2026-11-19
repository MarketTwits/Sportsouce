package com.markettwits.sportsouce.start.register.presentation.registration.member.components

import androidx.compose.runtime.Composable
import com.markettwits.core_ui.items.components.status.SauceStatusBadge
import com.markettwits.core_ui.items.components.status.SauceStatusTone

@Composable
internal fun RegistrationTeamBadge() {
    SauceStatusBadge("Из команды", tone = SauceStatusTone.Neutral, showIndicator = false)
}
