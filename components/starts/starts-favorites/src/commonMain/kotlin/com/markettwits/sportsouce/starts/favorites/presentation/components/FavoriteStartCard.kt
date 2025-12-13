package com.markettwits.sportsouce.starts.favorites.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.common.presentation.startStatusBackground
import com.markettwits.sportsouce.starts.common.presentation.startStatusCompactMessage

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
    OnBackgroundCard(
        modifier = modifier,
        onClick = { onItemClick(start) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Top - Image with overlays
            FavoriteStartImageSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                start = start,
                isDeleting = isDeleting,
                isRemoved = isRemoved,
                onFavoriteClick = onFavoriteClick,
                onAddToFavorites = onAddToFavorites
            )

            // Bottom - Info
            FavoriteStartInfoSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                start = start
            )
        }
    }
}

@Composable
private fun FavoriteStartImageSection(
    modifier: Modifier = Modifier,
    start: StartsListItem,
    isDeleting: Boolean = false,
    isRemoved: Boolean = false,
    onFavoriteClick: (StartsListItem) -> Unit,
    onAddToFavorites: (StartsListItem) -> Unit,
) {
    Box(modifier = modifier) {
        // Background image
        SubcomposeAsyncImage(
            model = imageRequestCrossfade(start.image),
            filterQuality = FilterQuality.Medium,
            contentDescription = start.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().clip(Shapes.medium),
            error = {
                if (start.image.isEmpty())
                    SubcomposeAsyncImageContent(
                        modifier = Modifier.fillMaxSize(),
                        painter = DefaultImages.EmptyImageStart()
                    )
                else
                    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer))
            },
            loading = {
                Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer))
            },
            success = {
                SubcomposeAsyncImageContent(modifier = Modifier.fillMaxSize())
            }
        )

        // Status and date badges (top left)
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(4.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Status badge
            Box(
                modifier = Modifier
                    .clip(Shapes.medium)
                    .background(startStatusBackground(start.statusCode.id).copy(alpha = 0.8f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = startStatusCompactMessage(start.statusCode.id),
                    color = Color.White,
                    fontFamily = FontNunito.bold(),
                    fontSize = 9.sp
                )
            }

            // Date badge
            Box(
                modifier = Modifier
                    .clip(Shapes.medium)
                    .background(SportSouceColor.SportSouceLighBlue.copy(alpha = 0.8f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = start.date,
                    color = Color.White,
                    fontFamily = FontNunito.semiBoldBold(),
                    fontSize = 9.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Favorite button or progress indicator (top right)
        Box(
            modifier = Modifier
                .padding(6.dp)
                .size(40.dp)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(
                    if (isRemoved) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
                    else SportSouceColor.SportSouceBlue.copy(alpha = 0.9f)
                ),
            contentAlignment = Alignment.Center
        ) {
            when {
                isDeleting -> {
                    androidx.compose.material3.CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(24.dp)
                    )
                }

                isRemoved -> {
                    IconButton(
                        onClick = { onAddToFavorites(start) },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.FavoriteBorder,
                            contentDescription = "Add to favorites again",
                            tint = SportSouceColor.SportSouceStartEndedPink,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                else -> {
                    IconButton(
                        onClick = { onFavoriteClick(start) },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Favorite,
                            contentDescription = "Remove from favorites",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteStartInfoSection(
    modifier: Modifier = Modifier,
    start: StartsListItem,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Title
        Text(
            text = start.name,
            fontSize = 16.sp,
            fontFamily = FontNunito.semiBoldBold(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Location
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = start.place,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 12.sp,
                fontFamily = FontNunito.medium(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Sport type and short distance hint
        if (start.kindOfSports.isNotEmpty() || start.distance.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (start.kindOfSports.isNotEmpty()) {
                    val sportColor =
                        com.markettwits.sportsouce.starts.common.presentation.startKindOfSportsColor(start.kindOfSports.first())
                    val sportIcon =
                        com.markettwits.sportsouce.starts.common.presentation.startKindOfSportsIcon(start.kindOfSports.first())

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(sportColor.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = sportIcon,
                                contentDescription = null,
                                tint = sportColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Text(
                            text = start.kindOfSports.first().name,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = FontNunito.semiBoldBold(),
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if (start.distance.isNotEmpty()) {
                    HtmlText(
                        text = start.distance,
                        modifier = Modifier.weight(1f, fill = false),
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = FontNunito.medium(),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}
