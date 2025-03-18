package com.markettwits.core_ui.items.components.divider

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SportSouceColor.DirtyWhite

@Composable
fun BaseDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 0.2.dp,
        color = DirtyWhite.copy(alpha = 0.2f)
    )
}