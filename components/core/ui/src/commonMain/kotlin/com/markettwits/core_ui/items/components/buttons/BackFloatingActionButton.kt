package com.markettwits.core_ui.items.components.buttons

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import com.markettwits.core_ui.items.theme.SauceTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BackFloatingActionButton(
    modifier: Modifier = Modifier,
    back: () -> Unit
) {
    SmallFloatingActionButton(
        modifier = modifier
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .padding(10.dp),
        containerColor = SauceTheme.colors.surfaceRaised,
        contentColor = SauceTheme.colors.textPrimary,
        onClick = back,
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Назад"
        )
    }
}