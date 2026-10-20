package com.markettwits.sportsouce.start.search.filter.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.buttons.SauceButtonVariant

@Composable
fun FilterButtonSelectionPanel(
    modifier: Modifier = Modifier,
    onClickReset: () -> Unit,
    onClickApply: () -> Unit,
) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        SauceButton("Сбросить", onClickReset, Modifier.weight(1f), variant = SauceButtonVariant.Outlined)
        SauceButton("Применить", onClickApply, Modifier.weight(1f))
    }
}
