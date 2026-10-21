package com.markettwits.sportsouce.auth.flow.internal.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.buttons.SauceButton

@Composable
internal fun AuthButton(
    modifier: Modifier = Modifier,
    title: String,
    enabled: Boolean,
    loading: Boolean,
    onClick: () -> Unit,
) {
    SauceButton(
        modifier = modifier,
        title = title,
        enabled = enabled,
        loading = loading,
        onClick = onClick,
    )
}
