package com.markettwits.core_ui.items.components.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceTheme

/** Information only; interactive filtering belongs to FilterChipBase. */
enum class SauceStatusTone { Neutral, Success, Warning, Danger }

@Composable
fun SauceStatusBadge(
    label: String,
    modifier: Modifier = Modifier,
    tone: SauceStatusTone = SauceStatusTone.Neutral,
    showIndicator: Boolean = true,
    centered: Boolean = false,
    singleLine: Boolean = false,
) {
    val style = MaterialTheme.typography.labelMedium
    val measured = rememberTextMeasurer().measure(label, style = style)
    val textWidth = with(LocalDensity.current) { measured.size.width.toDp() } + 1.dp
    val palette = SauceTheme.colors
    val colors = when (tone) {
        SauceStatusTone.Neutral -> palette.neutral
        SauceStatusTone.Success -> palette.success
        SauceStatusTone.Warning -> palette.warning
        SauceStatusTone.Danger -> palette.danger
    }
    Surface(
        modifier = modifier.semantics(mergeDescendants = true) {},
        color = colors.container,
        contentColor = colors.content,
        shape = SauceShapes.badge,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp, if (centered) Alignment.CenterHorizontally else Alignment.Start),
        ) {
            if (showIndicator) Box(Modifier.size(5.dp).background(colors.content, CircleShape))
            Text(text = label, modifier = Modifier.width(textWidth),
                style = style,
                maxLines = if (singleLine) 1 else Int.MAX_VALUE,
                overflow = if (singleLine) TextOverflow.Ellipsis else TextOverflow.Clip,
                textAlign = if (centered) TextAlign.Center else TextAlign.Start)
        }
    }
}
