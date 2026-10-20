package com.markettwits.core_ui.items.components.textField

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.components.timer.screens.TimerDialog
import com.markettwits.core_ui.items.components.timer.screens.TimerState
import com.markettwits.core_ui.items.components.timer.screens.rememberTimerPage

@Composable
fun TimeTextField(
    modifier: Modifier = Modifier,
    state : TimerState = rememberTimerPage(),
    enabled: Boolean = true,
    textFiled: @Composable (Modifier) -> Unit,
    onValueChanged: (String) -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }
    LaunchedEffect(enabled) { if (!enabled) openDialog = false }

    TimerDialog(
        timerState = state,
        isShowDialog = openDialog && enabled,
        onDismiss = {
            openDialog = false
        },
        onApply = {
            openDialog = false
            onValueChanged(state.timeState.toString())
        }
    )
    SelectorFieldAnchor(modifier = modifier, enabled = enabled, expanded = openDialog && enabled,
        onOpen = { openDialog = true }) {
        textFiled(Modifier)
    }
}
