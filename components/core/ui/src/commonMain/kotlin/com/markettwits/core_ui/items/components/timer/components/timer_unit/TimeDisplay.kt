package com.markettwits.core_ui.items.components.timer.components.timer_unit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.timer.domain.model.TimeData

@Composable
internal fun TimeDisplay(
    time: TimeData = TimeData(),
) {

    val timeUnitColor = if (time.isDataEmpty())
        MaterialTheme.colorScheme.outline
    else
        MaterialTheme.colorScheme.secondary

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimeDisplayUnit(
            time = time.hours,
            unit = "h",
            textColor = timeUnitColor
        )

        TimeDisplayUnit(
            time = time.mins,
            unit = "m",
            textColor = timeUnitColor
        )

        TimeDisplayUnit(
            time = time.secs,
            unit = "s",
            textColor = timeUnitColor
        )
    }
}