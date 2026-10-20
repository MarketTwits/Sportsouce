package com.markettwits.sportsouce.start.register.presentation.registration.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant
import com.markettwits.core_ui.items.theme.SauceSpacing

/** Same content edges as the registration form and payment summary. */
@Composable
internal fun RegistrationActions(
    modifier: Modifier = Modifier,
    primaryTitle: String? = null,
    onPrimaryClick: () -> Unit = {},
    secondaryTitle: String? = null,
    onSecondaryClick: () -> Unit = {},
    onBackClick: (() -> Unit)? = null,
    loading: Boolean = false,
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(SauceSpacing.large),
        verticalArrangement = Arrangement.spacedBy(SauceSpacing.small),
    ) {
        if (primaryTitle != null) {
            SauceButton(primaryTitle, onPrimaryClick, loading = loading)
        }
        if (secondaryTitle != null) {
            SauceButton(secondaryTitle, onSecondaryClick,
                variant = SauceButtonVariant.Outlined, enabled = !loading)
        }
        if (onBackClick != null) {
            SauceButton("Назад", onBackClick,
                variant = SauceButtonVariant.Text, enabled = !loading)
        }
    }
}
