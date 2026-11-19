package com.markettwits.core_ui.items.components.timer.components.timer_unit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.timer.domain.model.TimeData
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun TimeDisplay(time: TimeData = TimeData()) {
    val colors = SauceTheme.colors
    Row(Modifier.fillMaxWidth().background(colors.surfaceSubtle, SauceShapes.field).padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        TimeDisplayUnit(time.hours, "ч", Modifier.weight(1f))
        TimeDisplayUnit(time.mins, "мин", Modifier.weight(1f))
        TimeDisplayUnit(time.secs, "с", Modifier.weight(1f))
    }
}
