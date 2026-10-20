package com.markettwits.core_ui.items.components.timer.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.markettwits.core_ui.items.components.timer.domain.Keypad
import com.markettwits.core_ui.items.components.timer.domain.UiEvent
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
fun TimerDialog(timerState: TimerState, isShowDialog: Boolean, onDismiss: () -> Unit, onApply: () -> Unit) {
    if (isShowDialog) Dialog(onDismissRequest = onDismiss) {
        Surface(Modifier.widthIn(max = 400.dp).fillMaxWidth(),
            shape = SauceShapes.card, color = SauceTheme.colors.surfaceRaised, contentColor = SauceTheme.colors.textPrimary) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("Укажите время", Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(48.dp)) {
                        Icon(Icons.Default.Close, "Закрыть", modifier = Modifier.size(20.dp))
                    }
                }
                TimerSelectionScreen(
                    modifier = Modifier.fillMaxWidth().weight(1f, fill = false),
                    timeState = timerState.timeState,
                    onKeyClick = { key ->
                        if (key == Keypad.KeyApply) onApply()
                        else timerState.onEvent(UiEvent.OnKeyPressed(key))
                    },
                )
            }
        }
    }
}
