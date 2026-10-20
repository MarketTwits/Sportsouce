package com.markettwits.core_ui.items.components.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import com.markettwits.core_ui.items.theme.SauceTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShareFloatingActionButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    SmallFloatingActionButton(
        modifier = modifier
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .padding(10.dp),
        containerColor = SauceTheme.colors.surfaceRaised,
        contentColor = SauceTheme.colors.textPrimary,
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.Default.Share,
            contentDescription = "Поделиться"
        )
    }
}