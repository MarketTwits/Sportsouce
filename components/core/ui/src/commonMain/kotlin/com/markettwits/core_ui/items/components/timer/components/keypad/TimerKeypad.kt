package com.markettwits.core_ui.items.components.timer.components.keypad

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.timer.domain.Keypad
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun TimerKeypadScreen(onKeyClick: (Keypad) -> Unit) {
    val rows = listOf(
        listOf(Keypad.Key1, Keypad.Key2, Keypad.Key3),
        listOf(Keypad.Key4, Keypad.Key5, Keypad.Key6),
        listOf(Keypad.Key7, Keypad.Key8, Keypad.Key9),
        listOf(Keypad.Key00, Keypad.Key0, Keypad.KeyDelete),
    )
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { keys ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                keys.forEach { key ->
                    CircularKey(
                        key = key, modifier = Modifier.weight(1f), onClick = onKeyClick,
                        icon = if (key == Keypad.KeyDelete) Icons.AutoMirrored.Outlined.Backspace else null,
                        textColor = if (key == Keypad.KeyDelete) SauceTheme.colors.action else SauceTheme.colors.textPrimary,
                    )
                }
            }
        }
    }
}
