package com.markettwits.core_ui.items.components.timer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.components.timer.components.ScreenTitle
import com.markettwits.core_ui.items.components.timer.domain.Keypad
import com.markettwits.core_ui.items.components.timer.domain.UiEvent


@Composable
fun TimerDialog(
    timerState : TimerState,
    isShowDialog : Boolean,
    onDismiss : () -> Unit,
    onApply : () -> Unit,
) {
    if (isShowDialog){
        Dialog(
            onDismissRequest = onDismiss
        ){
            Column (
                modifier = Modifier
                    .clip(Shapes.medium)
                    .background(MaterialTheme.colorScheme.background),
            ) {
                TimerSelectionScreen(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    timeState = timerState.timeState,
                    onKeyClick = { key ->
                        if (key is Keypad.KeyApply)
                            onApply()
                        else
                            timerState.onEvent(
                                UiEvent.OnKeyPressed(key)
                            )
                    }
                )
            }
        }
    }
}