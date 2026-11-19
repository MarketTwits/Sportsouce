package com.markettwits.sportsouce.start.register.presentation.registration.member.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant

@Composable
fun MemberContinueButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClickContinue: () -> Unit,
    onClickDisabled: (() -> Unit)? = null,
) {
    // An incomplete form still opens the existing validation feedback on click.
    SauceButton(
        modifier = modifier,
        title = "Продолжить",
        variant = if (enabled) SauceButtonVariant.Primary else SauceButtonVariant.Soft,
        enabled = enabled || onClickDisabled != null,
        onClick = { if (enabled) onClickContinue() else onClickDisabled?.invoke() },
    )
}
