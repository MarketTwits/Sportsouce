package com.markettwits.sportsouce.auth.flow.internal.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant

@Composable
internal fun AuthConsumeRowContent(
    modifier: Modifier = Modifier,
    title: String,
    onClickConsume: () -> Unit,
) {
    SauceButton(title = title, onClick = onClickConsume, modifier = modifier, variant = SauceButtonVariant.Text)
}
