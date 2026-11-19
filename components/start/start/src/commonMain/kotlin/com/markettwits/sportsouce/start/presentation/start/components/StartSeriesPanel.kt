package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.markettwits.core_ui.items.components.buttons.SauceClickTarget
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.start.presentation.series.StartRelatedKind
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

@Composable
internal fun StartSeriesPanel(
    modifier: Modifier = Modifier,
    items: List<StartsListItem>,
    currentStartId: Int,
    onClickRelatedStarts: () -> Unit,
    onItemClick: (StartsListItem) -> Unit,
    kind: StartRelatedKind = StartRelatedKind.Series,
    previewItems: List<StartsListItem> = seriesPreview(items, currentStartId),
) {
    if (items.isEmpty()) return
    val colors = SauceTheme.colors
    val previewableCount = if (kind == StartRelatedKind.Archive) items.count { it.id != currentStartId } else items.size
    val showContinuation = previewItems.isNotEmpty() && previewableCount > previewItems.size
    val connector = colors.controlBorder.copy(alpha = .55f)
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(SauceSpacing.medium)) {
        Row(
            modifier = Modifier.width(IntrinsicSize.Max),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small),
        ) {
            Text(
                text = kind.title,
                modifier = Modifier.weight(1f).semantics { heading() },
                style = MaterialTheme.typography.titleMedium,
                color = colors.textPrimary,
            )
            Surface(shape = SauceShapes.badge, color = colors.surfaceSubtle, contentColor = colors.textSecondary) {
                Text(items.size.toString(), Modifier.padding(horizontal = 8.dp, vertical = 4.dp).width(IntrinsicSize.Max),
                    style = MaterialTheme.typography.labelMedium)
            }
        }

        Surface(shape = SauceShapes.card, color = colors.surfaceSubtle) {
            Column(Modifier.fillMaxWidth().padding(top = SauceSpacing.extraSmall)) {
                previewItems.forEachIndexed { index, item ->
                    StartTimelineEntry(
                        item = item,
                        isCurrent = item.id == currentStartId,
                        hasPrevious = index > 0,
                        hasNext = index < previewItems.lastIndex || showContinuation,
                        onClick = { onItemClick(item) },
                    )
                }
                SauceClickTarget(
                    onClick = onClickRelatedStarts,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(
                        topStart = CornerSize(0.dp),
                        topEnd = CornerSize(0.dp),
                        bottomStart = SauceShapes.card.bottomStart,
                        bottomEnd = SauceShapes.card.bottomEnd,
                    ),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)
                            .drawBehind {
                                if (showContinuation) {
                                    val x = timelineMarkerX()
                                    val fadeEnd = size.height / 2
                                    drawLine(
                                        brush = Brush.verticalGradient(listOf(connector, connector.copy(alpha = 0f)), endY = fadeEnd),
                                        start = Offset(x, 0f),
                                        end = Offset(x, fadeEnd),
                                        strokeWidth = TimelineStroke.toPx(),
                                    )
                                }
                            }
                            .padding(start = TimelineInset, end = SauceSpacing.medium,
                                top = SauceSpacing.small, bottom = SauceSpacing.small),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small),
                    ) {
                        Text(
                            text = allStartsLabel(items.size),
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.titleSmall,
                            color = colors.action,
                        )
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, Modifier.size(20.dp), tint = colors.action)
                    }
                }
            }
        }
    }
}

@Composable
private fun StartTimelineEntry(
    item: StartsListItem,
    isCurrent: Boolean,
    hasPrevious: Boolean,
    hasNext: Boolean,
    onClick: () -> Unit,
) {
    val colors = SauceTheme.colors
    var markerSeen by rememberSaveable(item.id) { mutableStateOf(false) }
    val markerProgress = if (isCurrent) animateFloatAsState(
        targetValue = if (markerSeen) 1f else 0f,
        animationSpec = tween(SauceMotion.progressMillis, easing = SauceMotion.progressEasing),
        label = "Current start marker",
    ) else null
    val content: @Composable () -> Unit = {
        Row(
            modifier = Modifier.fillMaxWidth()
                .drawBehind {
                    val x = timelineMarkerX()
                    val y = size.height / 2
                    val gap = if (isCurrent) 13.dp.toPx() else 7.dp.toPx()
                    val connector = colors.controlBorder.copy(alpha = .55f)
                    if (hasPrevious) drawLine(connector, Offset(x, 0f), Offset(x, y - gap), strokeWidth = TimelineStroke.toPx())
                    if (hasNext) drawLine(connector, Offset(x, y + gap), Offset(x, size.height), strokeWidth = TimelineStroke.toPx())
                    if (markerProgress != null) {
                        val progress = markerProgress.value
                        val center = Offset(x, y)
                        drawCircle(
                            colors.action.copy(alpha = .12f + .10f * progress),
                            radius = (6f + 3.5f * progress).dp.toPx(),
                            center = center,
                        )
                        drawCircle(colors.action, radius = (3.5f + 2f * progress).dp.toPx(), center = center)
                    } else {
                        drawCircle(colors.textMuted, radius = 3.dp.toPx(), center = Offset(x, y))
                    }
                }
                .padding(start = TimelineInset, end = SauceSpacing.medium,
                    top = SauceSpacing.small, bottom = SauceSpacing.small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SauceSpacing.medium),
        ) {
            val placeholder = DefaultImages.EmptyImageStart()
            AsyncImage(
                model = imageRequestCrossfade(item.image),
                contentDescription = null,
                placeholder = placeholder,
                error = placeholder,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(width = 48.dp, height = 72.dp).clip(SauceShapes.badge),
            )
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall)) {
                Text(item.name, Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleSmall,
                    color = colors.textPrimary)
                val date = item.displayDate
                val subtitle = when {
                    !isCurrent -> date
                    date.isBlank() -> "Вы здесь"
                    else -> "Вы здесь · $date"
                }
                if (subtitle.isNotBlank()) {
                    Text(subtitle, Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodySmall,
                        color = if (isCurrent) colors.action else colors.textSecondary)
                }
            }
            if (!isCurrent) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, Modifier.size(20.dp), tint = colors.textMuted)
            }
        }
    }
    if (isCurrent) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .onVisibilityChanged(minFractionVisible = .6f) { visible ->
                    if (visible) markerSeen = true
                }
                .semantics(mergeDescendants = true) { selected = true },
        ) { content() }
    } else {
        SauceClickTarget(onClick = onClick, modifier = Modifier.fillMaxWidth()) { content() }
    }
}

private val TimelineInset = 34.dp
private val TimelineStroke = 1.5.dp

private fun seriesPreview(items: List<StartsListItem>, currentStartId: Int): List<StartsListItem> {
    val limit = 3
    val currentIndex = items.indexOfFirst { it.id == currentStartId }
    val firstIndex = if (currentIndex >= limit) {
        (currentIndex - 1).coerceAtMost(items.size - limit)
    } else 0
    return items.drop(firstIndex).take(limit)
}

private fun DrawScope.timelineMarkerX(): Float =
    if (layoutDirection == LayoutDirection.Ltr) 18.dp.toPx() else size.width - 18.dp.toPx()

private fun allStartsLabel(count: Int): String = when {
    count % 100 in 11..14 -> "Все $count стартов"
    count % 10 == 1 -> "Показать $count старт"
    count % 10 in 2..4 -> "Все $count старта"
    else -> "Все $count стартов"
}
