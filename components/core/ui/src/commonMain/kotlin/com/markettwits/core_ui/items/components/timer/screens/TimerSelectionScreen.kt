package com.markettwits.core_ui.items.components.timer.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll as verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.buttons.SauceButton
import com.markettwits.core_ui.items.components.timer.components.keypad.TimerKeypadScreen
import com.markettwits.core_ui.items.components.timer.components.timer_unit.TimeDisplay
import com.markettwits.core_ui.items.components.timer.domain.Keypad
import com.markettwits.core_ui.items.components.timer.domain.model.TimeData

@Composable
internal fun TimerSelectionScreen(modifier: Modifier, timeState: TimeData, onKeyClick: (Keypad) -> Unit) {
    Column(modifier.heightIn(max = 560.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Column(Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            TimeDisplay(time = timeState)
            TimerKeypadScreen(onKeyClick = onKeyClick)
        }
        SauceButton(title = "Применить", enabled = !timeState.isDataEmpty(), onClick = { onKeyClick(Keypad.KeyApply) })
    }
}
