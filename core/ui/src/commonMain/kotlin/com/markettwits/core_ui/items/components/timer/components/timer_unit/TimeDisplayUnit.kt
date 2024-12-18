package com.markettwits.core_ui.items.components.timer.components.timer_unit

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.timer.domain.model.TimeUnit

@Composable
internal fun TimeDisplayUnit(
    time: TimeUnit = TimeUnit(),
    unit: String,
    textColor: Color = MaterialTheme.colorScheme.outline
) {

    Row(
        modifier = Modifier.wrapContentSize(),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = "${time.leftDigit}",
            modifier = Modifier.alignByBaseline(),
            style = TextStyle(
                fontSize = 55.sp,
                fontWeight = FontWeight.Medium,
                color = textColor,
            )
        )
        Text(
            text = "${time.rightDigit}",
            modifier = Modifier.alignByBaseline(),
            style = TextStyle(
                fontSize = 55.sp,
                fontWeight = FontWeight.Medium,
                color = textColor,
            ),
        )
        Text(
            text = unit,
            modifier = Modifier.alignByBaseline(),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = textColor,
            ),
        )
    }
}
