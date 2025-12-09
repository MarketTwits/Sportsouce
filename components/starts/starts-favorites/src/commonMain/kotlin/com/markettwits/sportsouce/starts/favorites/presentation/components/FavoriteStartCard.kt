package com.markettwits.sportsouce.starts.favorites.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
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
                    .height(180.dp),
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
                    .padding(12.dp),
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
                .padding(8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Status badge
            Box(
                modifier = Modifier
                    .clip(Shapes.medium)
                    .background(startStatusBackground(start.statusCode.id).copy(alpha = 0.95f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = startStatusCompactMessage(start.statusCode.id),
                    color = Color.White,
                    fontFamily = FontNunito.bold(),
                    fontSize = 12.sp
                )
            }

            // Date badge
            Box(
                modifier = Modifier
                    .clip(Shapes.medium)
                    .background(SportSouceColor.SportSouceLighBlue.copy(alpha = 0.95f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = start.date,
                    color = Color.White,
                    fontFamily = FontNunito.semiBoldBold(),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Favorite button or progress indicator (top right)
        Box(
            modifier = Modifier
                .padding(8.dp)
                .size(48.dp)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(
                    if (isRemoved) MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
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
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Add to favorites again",
                            tint = Color.White.copy(alpha = 0.5f),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                else -> {
                    IconButton(
                        onClick = { onFavoriteClick(start) },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Remove from favorites",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Title
        Text(
            text = start.name,
            fontSize = 20.sp,
            fontFamily = FontNunito.bold(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Location
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = start.place,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                fontFamily = FontNunito.medium(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Distances
        if (start.distance.isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Дистанции",
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 11.sp,
                    fontFamily = FontNunito.medium()
                )
                HtmlText(
                    text = start.distance,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontNunito.medium(),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Sport type and views
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Sport type with icon
            if (start.kindOfSports.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val sportColor =
                        com.markettwits.sportsouce.starts.common.presentation.startKindOfSportsColor(start.kindOfSports.first())
                    val sportIcon =
                        com.markettwits.sportsouce.starts.common.presentation.startKindOfSportsIcon(start.kindOfSports.first())

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(sportColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = sportIcon,
                            contentDescription = null,
                            tint = sportColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Text(
                        text = start.kindOfSports.first().name,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = FontNunito.semiBoldBold(),
                        fontSize = 14.sp
                    )
                }
            }

            // Views indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "${start.views}",
                    color = MaterialTheme.colorScheme.outline,
                    fontFamily = FontNunito.medium(),
                    fontSize = 13.sp
                )
            }
        }
    }
}
