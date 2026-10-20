package com.markettwits.core_ui.items.components.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.*
import kotlin.math.roundToInt

/** Null/non-finite means unknown progress; finite values are clamped to the unit interval. */
@Composable
fun SauceLinearProgressIndicator(
    label: String,
    progress: Float?,
    modifier: Modifier = Modifier,
    showValue: Boolean = true,
    valueLabel: String? = null,
) {
    val colors = SauceTheme.colors
    val value = progress?.takeIf { it.isFinite() }?.coerceIn(0f, 1f)
    val animated by animateFloatAsState(value ?: 0f,
        tween(SauceMotion.progressMillis, easing = SauceMotion.progressEasing))
    Column(
        modifier.fillMaxWidth().semantics(mergeDescendants = true) {
            contentDescription = label
            if (value != null && valueLabel != null) stateDescription = valueLabel
            progressBarRangeInfo = value?.let { ProgressBarRangeInfo(it, 0f..1f) }
                ?: ProgressBarRangeInfo.Indeterminate
        },
        verticalArrangement = Arrangement.spacedBy(SauceSpacing.small),
    ) {
        Row(Modifier.fillMaxWidth().clearAndSetSemantics {},
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
            Text(label, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary)
            if (showValue && value != null) {
                val text = valueLabel ?: "${(value * 100).roundToInt()}%"
                val style = MaterialTheme.typography.labelLarge
                val measured = rememberTextMeasurer().measure(text, style = style)
                val width = with(LocalDensity.current) { measured.size.width.toDp() } + 1.dp
                Text(text, Modifier.width(width), style = style, color = colors.textPrimary)
            }
        }
        val barModifier = Modifier.fillMaxWidth().height(8.dp).clearAndSetSemantics {}
        if (value != null) LinearProgressIndicator(
            progress = { animated }, modifier = barModifier,
            color = colors.action, trackColor = colors.surfaceSubtle,
            strokeCap = StrokeCap.Round, gapSize = 0.dp, drawStopIndicator = {},
        ) else LinearProgressIndicator(
            modifier = barModifier, color = colors.action,
            trackColor = colors.surfaceSubtle, strokeCap = StrokeCap.Round, gapSize = 0.dp,
        )
    }
}
