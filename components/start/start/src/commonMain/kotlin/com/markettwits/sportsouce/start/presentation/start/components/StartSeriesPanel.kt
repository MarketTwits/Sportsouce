package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
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
) {
    if (items.isNotEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            // Header with title and "Show more" button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Связанные старты",
                    fontSize = 18.sp,
                    fontFamily = FontNunito.bold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                // Show "Show more" or "Show all" button
                if (items.size > 3) {
                    val remainingCount = items.size - 3
                    Box(
                        modifier = Modifier
                            .clip(Shapes.small)
                            .clickable { onClickRelatedStarts() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Показать ещё $remainingCount",
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = FontNunito.semiBoldBold(),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                } else {
                    Box(
                        modifier = Modifier
                            .clip(Shapes.small)
                            .clickable { onClickRelatedStarts() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Показать всё",
                            fontSize = 12.sp,
                            fontFamily = FontNunito.semiBoldBold(),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            // Vertical list, showing only first 3 items
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items.take(3).forEach { item ->
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

@Composable
private fun SeriesCard(
    modifier: Modifier = Modifier,
    item: StartsListItem,
    isCurrentStart: Boolean = false,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        OnBackgroundCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            onClick = if (!isCurrentStart) onClick else null,
        ) {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                // Left accent bar for current start
                if (isCurrentStart) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.secondary)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Image with fixed width
                    Box(
                        modifier = Modifier
                            .width(70.dp)
                            .fillMaxHeight()
                            .clip(Shapes.medium)
                    ) {
                        SubcomposeAsyncImage(
                            model = imageRequestCrossfade(model = item.image),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            error = {
                                SubcomposeAsyncImageContent(
                                    modifier = Modifier,
                                    painter = DefaultImages.EmptyImageStart()
                                )
                            },
                            success = {
                                SubcomposeAsyncImageContent(modifier = Modifier)
                            }
                        )
                    }

                    // Content
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Title
                        Text(
                            text = item.name,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = FontNunito.bold(),
                            fontSize = 14.sp,
                            lineHeight = 18.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2
                        )

                        // Date
                        Text(
                            text = item.date,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                            fontFamily = FontNunito.medium(),
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Sport kind badges
                        if (item.kindOfSports.isNotEmpty()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                item.kindOfSports.take(2).forEach { sportKind ->
                                    Surface(
                                        color = startKindOfSportsColor(sportKind).copy(alpha = 0.15f),
                                        shape = Shapes.small
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(
                                                horizontal = 8.dp,
                                                vertical = 4.dp
                                            ),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                                        ) {
                                            Icon(
                                                imageVector = startKindOfSportsIcon(sportKind),
                                                contentDescription = null,
                                                tint = startKindOfSportsColor(sportKind),
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Text(
                                                text = sportKind.name,
                                                fontSize = 10.sp,
                                                fontFamily = FontNunito.semiBoldBold(),
                                                color = startKindOfSportsColor(sportKind),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (isCurrentStart) {
                        Surface(
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
                            shape = Shapes.small
                        ) {
                            Text(
                                text = "Вы здесь",
                                color = MaterialTheme.colorScheme.secondary,
                                maxLines = 1,
                                overflow = TextOverflow.Clip,
                                fontFamily = FontNunito.bold(),
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
