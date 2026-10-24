package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.list.SauceActionRow
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.common.presentation.startKindOfSportsColor
import com.markettwits.sportsouce.starts.common.presentation.startKindOfSportsIcon

@Composable
internal fun StartSeriesPanel(
    modifier: Modifier = Modifier,
    items: List<StartsListItem>,
    currentStartId: Int,
    onClickRelatedStarts: () -> Unit,
    onItemClick: (StartsListItem) -> Unit,
    title: String = "Серии",
    previewItems: List<StartsListItem> = items.take(3),
) {
    if (items.isNotEmpty()) {
        Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SauceActionRow(
                title = title, count = items.size.toString(), onClick = onClickRelatedStarts,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                previewItems.forEach { item ->
                    SeriesCard(
                        item = item,
                        isCurrentStart = item.id == currentStartId,
                        onClick = { onItemClick(item) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SeriesCard(
    modifier: Modifier = Modifier,
    item: StartsListItem,
    isCurrentStart: Boolean = false,
    onClick: () -> Unit,
) {
    val colors = SauceTheme.colors
    OnBackgroundCard(modifier.fillMaxWidth(), onClick = if (isCurrentStart) null else onClick) {
        Box(Modifier.fillMaxWidth()) {
            if (isCurrentStart) Box(Modifier.matchParentSize()) {
                Box(Modifier.width(4.dp).fillMaxHeight().background(colors.action))
            }
            Row(Modifier.fillMaxWidth().heightIn(min = 120.dp).padding(SauceSpacing.medium),
                horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small), verticalAlignment = Alignment.CenterVertically) {
                val placeholder = DefaultImages.EmptyImageStart()
                AsyncImage(model = imageRequestCrossfade(item.image), contentDescription = null,
                    placeholder = placeholder, error = placeholder, contentScale = ContentScale.Crop,
                    modifier = Modifier.size(70.dp, 96.dp).clip(SauceShapes.field))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(item.name, style = MaterialTheme.typography.titleSmall, color = colors.textPrimary,
                        maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text(item.displayDate, style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        item.kindOfSports.take(2).forEach { sport ->
                            val color = startKindOfSportsColor(sport)
                            Surface(color = color.copy(alpha = 0.15f), shape = SauceShapes.badge) {
                                Row(Modifier.padding(horizontal = 6.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(startKindOfSportsIcon(sport), null, Modifier.size(14.dp), tint = color)
                                    Text(sport.name, style = MaterialTheme.typography.labelSmall, color = color,
                                        maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                            }
                        }
                    }
                }
                if (isCurrentStart) Surface(
                    modifier = Modifier.width(IntrinsicSize.Max),
                    color = colors.actionSoft, shape = SauceShapes.badge,
                ) {
                    Text("Вы здесь", Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall, color = colors.action, maxLines = 1)
                }
            }
        }
    }
}
