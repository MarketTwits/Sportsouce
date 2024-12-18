package com.markettwits.core_ui.items.components.timer.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.components.timer.components.keypad.TimerKeypadScreen
import com.markettwits.core_ui.items.components.timer.components.timer_unit.TimeDisplay
import com.markettwits.core_ui.items.components.timer.domain.Keypad
import com.markettwits.core_ui.items.components.timer.domain.model.TimeData

@Composable
internal fun TimerSelectionScreen(
    modifier: Modifier,
    timeState: TimeData,
    onKeyClick: (Keypad) -> Unit
) {
    val isPlayButtonVisible = timeState.isDataEmpty().not()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimeDisplay(
            time = timeState,
        )

        Spacer(modifier = Modifier.height(24.dp))

        TimerKeypadScreen(
            onKeyClick = { key ->
                onKeyClick(key)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(
            visible = isPlayButtonVisible,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            ButtonContentBase(
                title = "Применить",
                shape = Shapes.medium,
                containerColor = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.onSecondary,
                onClick = {
                    onKeyClick(Keypad.KeyApply)
                }
            )
        }
    }
}