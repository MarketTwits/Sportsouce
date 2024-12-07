package com.markettwits.start.register.presentation.registration.presentation.components.distsance.additional_fields

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
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
    textFiled: @Composable (Modifier) -> Unit,
    onValueChanged: (String) -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }

    TimerDialog(
        timerState = state,
        isShowDialog = openDialog,
        onDismiss = {
            openDialog = false
        },
        onApply = {
            openDialog = false
            onValueChanged(state.timeState.toString())
        }
    )
    textFiled(modifier.clickable {
        openDialog = true
    })
}
