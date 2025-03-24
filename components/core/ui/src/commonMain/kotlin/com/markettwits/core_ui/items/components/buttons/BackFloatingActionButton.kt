package com.markettwits.core_ui.items.components.buttons

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
        modifier = modifier.padding(10.dp),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.tertiary,
        onClick = {
            back()
        },
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Floating action button."
        )
    }
}