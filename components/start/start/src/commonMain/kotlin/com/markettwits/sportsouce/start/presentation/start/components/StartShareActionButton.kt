package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceFloatingActionButton
import com.markettwits.core_ui.items.components.buttons.SauceFloatingActionGlass

@Composable
internal fun StartShareActionButton(glass: SauceFloatingActionGlass?, onClick: () -> Unit) {
    SauceFloatingActionButton(
        glass = glass,
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.Default.Share,
            contentDescription = "Поделиться стартом"
        )
    }
}
