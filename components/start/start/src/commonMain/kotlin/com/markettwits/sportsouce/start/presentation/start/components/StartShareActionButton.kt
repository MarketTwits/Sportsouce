package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun StartShareActionButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    SmallFloatingActionButton(
        modifier = modifier
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .padding(vertical = 10.dp)
            .padding(horizontal = 4.dp),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.tertiary,
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.Default.Share,
            contentDescription = "Share"
        )
    }
}