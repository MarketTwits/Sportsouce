package com.markettwits.core_ui.items.components.timer.screens

import androidx.compose.runtime.*
import com.markettwits.core_ui.items.components.timer.domain.Keypad
import com.markettwits.core_ui.items.components.timer.domain.UiEvent
import com.markettwits.core_ui.items.components.timer.domain.model.TimeData
import com.markettwits.core_ui.items.components.timer.domain.model.TimeUnit

class TimerState(
    timeState: MutableState<TimeData> = mutableStateOf(TimeData()),
) {
    var timeState by timeState

    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.OnKeyPressed -> onKeyPress(event.key)
        }
    }

    private fun onKeyPress(key: Keypad) {
        when (key) {
            Keypad.KeyDelete -> {
                if (!timeState.isDataEmpty()) deleteTime()
            }
            Keypad.Key00 -> {
                if (!timeState.isDataEmpty() &&
                    !timeState.isHoursHalfFull() &&
                    !timeState.isDataFull()
                ) {
                    addTime(Keypad.Key0.value)
                    addTime(Keypad.Key0.value)
                }
            }
            Keypad.Key0 -> {
                if (!timeState.isDataEmpty() && !timeState.isDataFull()) {
                    addTime(key.value)
                }
            }
            else -> {
                if (!timeState.isDataFull()) addTime(key.value)
            }
        }
    }

    private fun deleteTime() {
        val secs = TimeUnit(
            rightDigit = timeState.secs.leftDigit,
            leftDigit = timeState.mins.rightDigit,
        )
        val mins = TimeUnit(
            rightDigit = timeState.mins.leftDigit,
            leftDigit = timeState.hours.rightDigit,
        )
        val hours = TimeUnit(
            rightDigit = timeState.hours.leftDigit,
            leftDigit = 0,
        )
        timeState = timeState.copy(
            hours = hours,
            mins = mins,
            secs = secs
        )
    }

    private fun addTime(value: String) {
        val intValue = value.toInt()
        val hours = TimeUnit(
            leftDigit = timeState.hours.rightDigit,
            rightDigit = timeState.mins.leftDigit,
        )
        val mins = TimeUnit(
            leftDigit = timeState.mins.rightDigit,
            rightDigit = timeState.secs.leftDigit,
        )
        val secs = TimeUnit(
            leftDigit = timeState.secs.rightDigit,
            rightDigit = intValue,
        )
        timeState = timeState.copy(
            hours = hours,
            mins = mins,
            secs = secs
        )
    }
}
@Composable
fun rememberTimerPage(
    timeState : MutableState<TimeData> = mutableStateOf(TimeData()),
): TimerState {
    return remember {
        TimerState(timeState)
    }
}