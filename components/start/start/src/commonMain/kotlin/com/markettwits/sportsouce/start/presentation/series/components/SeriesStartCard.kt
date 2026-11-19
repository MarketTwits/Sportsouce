package com.markettwits.sportsouce.start.presentation.series.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.common.presentation.StartStatusBadge
import com.markettwits.sportsouce.starts.common.presentation.startKindOfSportsColor
import com.markettwits.sportsouce.starts.common.presentation.startKindOfSportsIcon

@Composable
internal fun SeriesStartCard(
    modifier: Modifier = Modifier,
    item: StartsListItem,
    isCurrentStart: Boolean = false,
    onClick: () -> Unit,
) {
    val colors = SauceTheme.colors
    Surface(
        modifier = modifier.fillMaxWidth().semantics {
            selected = isCurrentStart
            if (isCurrentStart) stateDescription = "Основной старт"
        },
        shape = SauceShapes.card,
        color = if (isCurrentStart) colors.actionSoft else colors.surface,
        contentColor = colors.textPrimary,
        border = BorderStroke(if (isCurrentStart) 2.dp else 1.dp,
            if (isCurrentStart) colors.brand else colors.controlBorder),
        onClick = onClick,
    ) {
        BoxWithConstraints(Modifier.fillMaxWidth().padding(SauceSpacing.large)) {
            val stacked = maxWidth < 240.dp * LocalDensity.current.fontScale
            val posterWidth = (maxWidth * 0.35f).coerceAtMost(150.dp)
            val placeholder = DefaultImages.EmptyImageStart()
            val poster: @Composable (Modifier) -> Unit = { imageModifier ->
                AsyncImage(
                    model = imageRequestCrossfade(item.image), contentDescription = null,
                    placeholder = placeholder, error = placeholder, contentScale = ContentScale.Crop,
                    modifier = imageModifier.clip(SauceShapes.field),
                )
            }
            if (stacked) {
                Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(SauceSpacing.large)) {
                    poster(Modifier.fillMaxWidth().height(160.dp))
                    SeriesStartInfo(item)
                }
            } else {
                Row(Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(SauceSpacing.large)) {
                    poster(Modifier.width(posterWidth).height(200.dp))
                    SeriesStartInfo(item, Modifier.weight(1f))
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SeriesStartInfo(item: StartsListItem, modifier: Modifier = Modifier) {
    val colors = SauceTheme.colors
    val density = LocalDensity.current
    val measurer = rememberTextMeasurer()
    val sportStyle = MaterialTheme.typography.labelSmall
    val viewsStyle = MaterialTheme.typography.bodySmall
    val views = item.views.toString()
    val viewsWidth = with(density) { measurer.measure(views, viewsStyle).size.width.toDp() } + 1.dp
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
        FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small),
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
            StartStatusBadge(item.statusCode.id, Modifier.align(Alignment.CenterVertically), compact = true)
            item.kindOfSports.take(2).forEach { sport ->
                val color = startKindOfSportsColor(sport)
                val textWidth = with(density) { measurer.measure(sport.name, sportStyle).size.width.toDp() } + 1.dp
                Surface(color = color.copy(alpha = 0.15f), shape = SauceShapes.badge) {
                    Row(Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(startKindOfSportsIcon(sport), null, Modifier.size(16.dp), tint = color)
                        Text(sport.name, Modifier.width(textWidth), style = sportStyle, color = color)
                    }
                }
            }
        }
        Text(item.name, Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleSmall, color = colors.textPrimary)
        Text(item.displayDate, Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
        if (item.distance.isNotBlank()) HtmlText(text = item.distance, modifier = Modifier.fillMaxWidth(),
            color = colors.textSecondary, style = MaterialTheme.typography.bodySmall,
            maxLines = 2, overflow = TextOverflow.Ellipsis, showImages = false)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
            if (item.place.isNotBlank()) {
                Row(Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Outlined.LocationOn, null, Modifier.size(16.dp), tint = colors.textMuted)
                    Text(item.place, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall,
                        color = colors.textSecondary, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
            } else Spacer(Modifier.weight(1f))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Outlined.Visibility, "Просмотры", Modifier.size(16.dp), tint = colors.textMuted)
                Text(views, Modifier.width(viewsWidth), style = viewsStyle, color = colors.textSecondary)
            }
        }
    }
}
