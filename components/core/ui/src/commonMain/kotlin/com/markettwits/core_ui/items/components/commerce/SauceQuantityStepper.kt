package com.markettwits.core_ui.items.components.commerce

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.*
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun SauceQuantityStepper(count: Int, onDecrease: () -> Unit, onIncrease: () -> Unit,
    modifier: Modifier = Modifier, canIncrease: Boolean = true, canDecrease: Boolean = count > 0) {
    Surface(modifier, shape = SauceShapes.field, color = SauceTheme.colors.surfaceSubtle) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            IconButton(onDecrease, enabled = canDecrease, modifier = Modifier.size(SauceSizes.touchTarget)) {
                Icon(Icons.Default.Remove, "Уменьшить количество", tint = if (canDecrease) SauceTheme.colors.action else SauceTheme.colors.disabledContent)
            }
            RollingQuantity(count)
            IconButton(onIncrease, enabled = canIncrease, modifier = Modifier.size(SauceSizes.touchTarget)) {
                Icon(Icons.Default.Add, "Увеличить количество", tint = if (canIncrease) SauceTheme.colors.action else SauceTheme.colors.disabledContent)
            }
        }
    }
}

@Composable
private fun RollingQuantity(count: Int) {
    var position by remember { mutableDoubleStateOf(count.toDouble()) }
    val visiblePlaces by remember {
        derivedStateOf {
            maxOf(floor(position).toLong().toString().length, ceil(position).toLong().toString().length)
        }
    }
    val places = maxOf(2, visiblePlaces, count.toString().length)

    LaunchedEffect(count) {
        val start = position
        val distance = count - start
        // Resume from the visible frame on a new tap. Large external changes can skip the intermediate numbers.
        if (distance != 0.0 && abs(distance) <= 10) {
            animate(0f, 1f, animationSpec = tween(SauceMotion.feedbackMillis, easing = SauceMotion.feedbackEasing)) { progress, _ ->
                position = start + distance * progress
            }
        }
        position = count.toDouble()
    }

    val style = MaterialTheme.typography.titleSmall
    val color = SauceTheme.colors.textPrimary
    val measurer = rememberTextMeasurer(cacheSize = 11)
    val glyphs = remember(measurer, style) {
        "0123456789-".associateWith { measurer.measure(it.toString(), style, maxLines = 1) }
    }
    val digitWidth = glyphs.values.maxOf { it.size.width }
    val lineHeight = glyphs.values.maxOf { it.size.height }
    val density = LocalDensity.current

    Canvas(
        Modifier.widthIn(min = 24.dp).padding(horizontal = 4.dp)
            .size(with(density) { (places * digitWidth).toDp() }, with(density) { lineHeight.toDp() })
            .clearAndSetSemantics {
                contentDescription = "Количество: $count"
                liveRegion = LiveRegionMode.Polite
            },
    ) {
        val value = position
        val lower = floor(value).toLong()
        val fraction = (value - lower).toFloat()
        val from = lower.toString()
        val to = if (fraction == 0f) from else (lower + 1).toString()
        val visibleWidth = digitWidth * (from.length + (to.length - from.length) * fraction)
        val right = (size.width + visibleWidth) / 2f
        val centerY = (size.height - lineHeight) / 2f

        clipRect {
            for (place in 0 until maxOf(from.length, to.length)) {
                val oldDigit = from.getOrNull(from.lastIndex - place)
                val newDigit = to.getOrNull(to.lastIndex - place)
                val centerX = right - (place + 0.5f) * digitWidth

                fun drawDigit(digit: Char?, offset: Float) {
                    val layout = digit?.let { glyphs[it] } ?: return
                    drawText(layout, color = color, topLeft = Offset(centerX - layout.size.width / 2f, centerY + offset))
                }

                if (oldDigit == newDigit) {
                    drawDigit(oldDigit, 0f)
                } else {
                    drawDigit(oldDigit, fraction * lineHeight)
                    drawDigit(newDigit, (fraction - 1f) * lineHeight)
                }
            }
        }
    }
}
