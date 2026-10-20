package com.markettwits.sportsouce.starts.favorites.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.components.progress.SauceSkeleton
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.*
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.common.presentation.StartStatusBadge
import com.markettwits.sportsouce.starts.common.presentation.startKindOfSportsColor
import com.markettwits.sportsouce.starts.common.presentation.startKindOfSportsIcon

@Composable
fun FavoriteStartCard(
    modifier: Modifier = Modifier,
    start: StartsListItem,
    isDeleting: Boolean = false,
    isRemoved: Boolean = false,
    onItemClick: (StartsListItem) -> Unit,
    onFavoriteClick: (StartsListItem) -> Unit,
    onAddToFavorites: (StartsListItem) -> Unit,
) {
    val colors = SauceTheme.colors
    val favorite: @Composable () -> Unit = {
        FavoriteAction(isDeleting, isRemoved) {
            if (isRemoved) onAddToFavorites(start) else onFavoriteClick(start)
        }
    }
    OnBackgroundCard(modifier = modifier, onClick = { onItemClick(start) }) {
        Column(Modifier.padding(SauceSpacing.medium), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                if (maxWidth < 280.dp * LocalDensity.current.fontScale) {
                    Column(verticalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                        FavoriteTitle(start.name)
                        Row(horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                            FavoritePoster(start.image, Modifier.size(56.dp, 64.dp))
                            Box(Modifier.weight(1f).align(Alignment.CenterVertically)) { FavoriteDate(start.date, maxLines = 3) }
                            favorite()
                        }
                    }
                } else Row(horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small)) {
                    FavoritePoster(start.image, Modifier.size(72.dp, 88.dp))
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(SauceSpacing.extraSmall)) {
                        FavoriteTitle(start.name)
                        FavoriteDate(start.date)
                    }
                    favorite()
                }
            }

            if (start.place.isNotBlank()) Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.Top) {
                Icon(Icons.Outlined.LocationOn, null, Modifier.padding(top = 2.dp).size(16.dp), tint = colors.textMuted)
                Text(start.place, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            FavoriteSportAndDistance(start)
            HorizontalDivider(color = colors.border)
            StartStatusBadge(statusCode = start.statusCode.id, compact = true)
        }
    }
}

@Composable
private fun FavoriteTitle(title: String) {
    Text(title, style = MaterialTheme.typography.titleSmall, color = SauceTheme.colors.textPrimary,
        maxLines = 2, overflow = TextOverflow.Ellipsis)
}

@Composable
private fun FavoriteDate(date: String, maxLines: Int = 2) {
    if (date.isNotBlank()) Text(date, style = MaterialTheme.typography.bodySmall,
        color = SauceTheme.colors.textSecondary, maxLines = maxLines, overflow = TextOverflow.Ellipsis)
}

@Composable
private fun FavoriteSportAndDistance(start: StartsListItem) {
    val sport = start.kindOfSports.firstOrNull()
    if (sport == null && start.distance.isBlank()) return
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val sportMaxWidth = maxWidth * 0.45f
        if (maxWidth < 280.dp * LocalDensity.current.fontScale) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (sport != null) FavoriteSport(sport)
                FavoriteDistance(start.distance)
            }
        } else Row(horizontalArrangement = Arrangement.spacedBy(SauceSpacing.small), verticalAlignment = Alignment.CenterVertically) {
            if (sport != null) FavoriteSport(sport, Modifier.widthIn(max = sportMaxWidth))
            FavoriteDistance(start.distance, Modifier.weight(1f))
        }
    }
}

@Composable
private fun FavoriteSport(sport: StartsListItem.KindOfSport, modifier: Modifier = Modifier) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(startKindOfSportsIcon(sport), null, Modifier.size(16.dp), tint = startKindOfSportsColor(sport))
        Text(sport.name, Modifier.weight(1f, fill = false), style = MaterialTheme.typography.bodySmall,
            color = SauceTheme.colors.textSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun FavoriteDistance(distance: String, modifier: Modifier = Modifier) {
    if (distance.isNotBlank()) HtmlText(text = distance, modifier = modifier, style = MaterialTheme.typography.bodySmall,
        color = SauceTheme.colors.textMuted, maxLines = 1, overflow = TextOverflow.Ellipsis, showImages = false)
}

@Composable
private fun FavoriteAction(isDeleting: Boolean, isRemoved: Boolean, onClick: () -> Unit) {
    val colors = SauceTheme.colors
    IconButton(
        onClick = onClick,
        enabled = !isDeleting,
        modifier = Modifier.size(SauceSizes.touchTarget).semantics {
            contentDescription = when {
                isDeleting -> "Обновление избранного"
                isRemoved -> "Добавить в избранное"
                else -> "Убрать из избранного"
            }
            stateDescription = if (isRemoved) "Не в избранном" else "В избранном"
            if (isDeleting) progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = colors.surfaceSubtle,
            contentColor = if (isRemoved) colors.textSecondary else colors.danger.content,
            disabledContainerColor = colors.surfaceSubtle,
        ),
    ) {
        if (isDeleting) CircularProgressIndicator(Modifier.size(20.dp), color = colors.textMuted, strokeWidth = 2.dp)
        else Icon(if (isRemoved) Icons.Rounded.FavoriteBorder else Icons.Rounded.Favorite, null, Modifier.size(22.dp))
    }
}

@Composable
private fun FavoritePoster(image: String, modifier: Modifier) {
    SubcomposeAsyncImage(
        model = imageRequestCrossfade(image.takeIf(String::isNotBlank)),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(SauceShapes.badge).background(SauceTheme.colors.surfaceSubtle),
        loading = { SauceSkeleton(Modifier.fillMaxSize(), SauceShapes.badge) },
        error = { SubcomposeAsyncImageContent(painter = DefaultImages.EmptyImageStart()) },
        success = { SubcomposeAsyncImageContent() },
    )
}
