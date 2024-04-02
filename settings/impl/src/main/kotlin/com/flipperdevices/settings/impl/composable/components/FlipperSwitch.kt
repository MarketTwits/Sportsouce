package com.flipperdevices.settings.impl.composable.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FlipperSwitch(
    state: Boolean,
    modifier: Modifier = Modifier,
    onSwitchState: (Boolean) -> Unit
) {
    Switch(
        modifier = modifier.padding(horizontal = 12.dp),
        checked = state,
        onCheckedChange = onSwitchState,
    )
}