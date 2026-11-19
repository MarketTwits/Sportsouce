package com.markettwits.sportsouce.start.presentation.result.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.divider.BaseDivider
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.start.presentation.common.components.*
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ResultCard(modifier: Modifier = Modifier, result: MemberResult, onClickMemberResult: (MemberResult) -> Unit) {
    StartListCard(modifier.fillMaxWidth(), onClick = { onClickMemberResult(result) }) {
        BoxWithConstraints(Modifier.fillMaxWidth().padding(12.dp)) {
            val density = LocalDensity.current
            val numberStyle = MaterialTheme.typography.titleMedium.copy(fontFeatureSettings = "tnum")
            val measuredPlace = rememberTextMeasurer().measure(result.place.toString(), style = numberStyle)
            val placeWidth = maxOf(48.dp, with(density) { measuredPlace.size.width.toDp() } + 16.dp)
            val inlinePlacement = maxWidth >= placeWidth + 180.dp * density.fontScale
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (inlinePlacement) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ResultPlacement(result.place, Modifier.width(placeWidth))
                        ResultIdentity(result, Modifier.weight(1f))
                    }
                } else {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        ResultPlacement(result.place, Modifier.width(placeWidth))
                        Spacer(Modifier.weight(1f))
                        ResultDisclosure()
                    }
                    ResultIdentity(result, showDisclosure = false)
                }
                if (result.distance.isNotBlank() || result.bodyNumber.isNotBlank()) {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        StartListTag(result.distance)
                        StartListInlineMetadata(
                            result.bodyNumber.takeIf { it.isNotBlank() }?.let { "№ $it" }.orEmpty(),
                            modifier = Modifier.align(Alignment.CenterVertically),
                        )
                    }
                }
                if (result.result.isNotBlank() || result.shift.isNotBlank()) {
                    BaseDivider()
                    ResultTiming(result.result, result.shift)
                }
            }
        }
    }
}

@Composable
private fun ResultIdentity(result: MemberResult, modifier: Modifier = Modifier, showDisclosure: Boolean = true) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(result.name.replace("\\", "\n"), Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary)
            if (result.group.isNotBlank()) Text(result.group, Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodySmall, color = SauceTheme.colors.textMuted)
            StartListMetadata("Команда", result.team)
        }
        if (showDisclosure) ResultDisclosure()
    }
}

@Composable
private fun ResultDisclosure() {
    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null,
        tint = SauceTheme.colors.textMuted, modifier = Modifier.size(24.dp))
}

@Composable
private fun ResultPlacement(place: Int, modifier: Modifier = Modifier) {
    val palette = SauceTheme.colors
    val container = when (place) {
        1 -> palette.brandContainer
        2, 3 -> palette.actionSoft
        else -> palette.surfaceSubtle
    }
    val content = when (place) {
        1 -> palette.onBrandContainer
        2, 3 -> palette.onActionSoft
        else -> palette.textPrimary
    }
    Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Surface(Modifier.fillMaxWidth(), shape = SauceShapes.field, color = container, contentColor = content) {
            Text(place.toString(), Modifier.padding(vertical = 8.dp).fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium.copy(fontFeatureSettings = "tnum"), textAlign = TextAlign.Center)
        }
        Text("Место", Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodySmall,
            color = palette.textMuted, textAlign = TextAlign.Center)
    }
}

/** A protocol-style timing line: value baselines align, captions sit underneath. */
@Composable
private fun ResultTiming(time: String, shift: String) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val palette = SauceTheme.colors
        val density = LocalDensity.current
        val measurer = rememberTextMeasurer()
        val shiftStyle = MaterialTheme.typography.bodyMedium.copy(fontFeatureSettings = "tnum")
        val captionStyle = MaterialTheme.typography.bodySmall
        val shiftWidthPx = maxOf(measurer.measure(shift, style = shiftStyle).size.width,
            measurer.measure("Отставание", style = captionStyle).size.width) + 1
        val styles = listOf(MaterialTheme.typography.titleLarge, MaterialTheme.typography.titleMedium, MaterialTheme.typography.titleSmall)
            .map { it.copy(fontFeatureSettings = "tnum") }
        val gapPx = with(density) { 16.dp.roundToPx() }
        val horizontal = time.isNotBlank() && shift.isNotBlank() &&
            measurer.measure(time, style = styles.first()).size.width + shiftWidthPx + gapPx <= constraints.maxWidth
        val timeWidthPx = if (horizontal) constraints.maxWidth - shiftWidthPx - gapPx else constraints.maxWidth
        val timeStyle = styles.firstOrNull { measurer.measure(time, style = it).size.width <= timeWidthPx } ?: styles.last()
        if (horizontal) {
            val shiftWidth = with(density) { shiftWidthPx.toDp() }
            Column(Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(time, Modifier.weight(1f).alignByBaseline(), style = timeStyle, color = palette.action)
                    Text(shift, Modifier.width(shiftWidth).alignByBaseline(), style = shiftStyle, color = palette.textSecondary, textAlign = TextAlign.End)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Результат", Modifier.weight(1f), style = captionStyle, color = palette.textMuted)
                    Text("Отставание", Modifier.width(shiftWidth), style = captionStyle, color = palette.textMuted, textAlign = TextAlign.End)
                }
            }
        } else {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (time.isNotBlank()) Column(Modifier.fillMaxWidth()) {
                    Text(time, Modifier.fillMaxWidth(), style = timeStyle, color = palette.action)
                    Text("Результат", Modifier.fillMaxWidth(), style = captionStyle, color = palette.textMuted)
                }
                if (shift.isNotBlank()) Column(Modifier.fillMaxWidth()) {
                    Text(shift, Modifier.fillMaxWidth(), style = shiftStyle, color = palette.textSecondary)
                    Text("Отставание", Modifier.fillMaxWidth(), style = captionStyle, color = palette.textMuted)
                }
            }
        }
    }
}
