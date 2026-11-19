package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.list.SauceActionRow

@Composable
internal fun StartConditionButton(modifier: Modifier = Modifier, fileName: String = "Положение", onClick: () -> Unit) {
    SauceActionRow(fileName, onClick, modifier, icon = Icons.Outlined.Description)
}
