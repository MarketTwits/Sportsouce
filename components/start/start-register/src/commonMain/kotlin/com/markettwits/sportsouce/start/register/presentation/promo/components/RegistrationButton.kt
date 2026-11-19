package com.markettwits.sportsouce.start.register.presentation.promo.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.buttons.SauceButton

@Composable
fun RegistrationButton(
    modifier: Modifier = Modifier,
    title: String,
    isEnabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit,
) {
    SauceButton(title, onClick, modifier, enabled = isEnabled, loading = isLoading)
}
