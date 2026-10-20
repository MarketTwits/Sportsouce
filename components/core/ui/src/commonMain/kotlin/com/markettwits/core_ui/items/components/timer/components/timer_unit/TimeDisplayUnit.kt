package com.markettwits.core_ui.items.components.timer.components.timer_unit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.timer.domain.model.TimeUnit
import com.markettwits.core_ui.items.theme.SauceTheme

@Composable
internal fun TimeDisplayUnit(
    time: TimeUnit = TimeUnit(),
    unit: String,
    modifier: Modifier = Modifier,
    textColor: Color = SauceTheme.colors.textPrimary,
) {
    Column(modifier.semantics(mergeDescendants = true) {},
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(time.toString(), Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center, color = textColor)
        Text(unit, Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center, color = SauceTheme.colors.textMuted)
    }
}
