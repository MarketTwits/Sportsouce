package com.markettwits.sportsouce.start.presentation.series.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.common.presentation.startKindOfSportsColor
import com.markettwits.sportsouce.starts.common.presentation.startKindOfSportsIcon
import com.markettwits.sportsouce.starts.common.presentation.startStatusBackground
import com.markettwits.sportsouce.starts.common.presentation.startStatusCompactMessage

@Composable
internal fun SeriesStartCard(
    modifier: Modifier = Modifier,
    item: StartsListItem,
    isCurrentStart: Boolean = false,
    isLast: Boolean = false,
    onClick: () -> Unit,
) {
    val connectorColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.25f)
    var cardHeightPx by remember { mutableStateOf(0) }
    val density = androidx.compose.ui.platform.LocalDensity.current

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        // Connection line from center of card to next card (dynamic size)
        if (!isLast && cardHeightPx > 0) {
            val cardHeightDp = with(density) { cardHeightPx.toDp() }
            Canvas(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(cardHeightDp + 60.dp)
            ) {
                val startY = cardHeightPx / 2f

                drawLine(
                    color = connectorColor,
                    start = Offset(size.width / 2, startY),
                    end = Offset(size.width / 2, size.height),
                    strokeWidth = 6.dp.toPx()
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Card content
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 240.dp)
                        .onSizeChanged { size ->
                            cardHeightPx = size.height
                        }
                        .background(MaterialTheme.colorScheme.background),
                    color = if (isCurrentStart) startStatusBackground(item.statusCode.id).copy(alpha = 0.25f) else Color.Transparent,
                    onClick = onClick
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Image with adaptive width
                            Box(
                                modifier = Modifier
                                    .weight(0.35f)
                                    .fillMaxHeight()
                                    .widthIn(max = 150.dp)
                                    .clip(RoundedCornerShape(12.dp))
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
                                modifier = Modifier.weight(0.65f),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Status badge and sport badges in one row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Status badge
                                    Surface(
                                        color = startStatusBackground(item.statusCode.id),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = startStatusCompactMessage(item.statusCode.id),
                                            color = Color.White,
                                            fontFamily = FontNunito.bold(),
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }

                                    // Sport type badges
                                    if (item.kindOfSports.isNotEmpty()) {
                                        item.kindOfSports.take(2).forEach { sport ->
                                            val sportColor = startKindOfSportsColor(sport)
                                            val sportIcon = startKindOfSportsIcon(sport)

                                            Surface(
                                                color = sportColor.copy(alpha = 0.15f),
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = sportIcon,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(12.dp),
                                                        tint = sportColor
                                                    )
                                                    Text(
                                                        text = sport.name,
                                                        color = sportColor,
                                                        fontFamily = FontNunito.semiBoldBold(),
                                                        fontSize = 10.sp,
                                                        maxLines = 1
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                // Title
                                Text(
                                    text = item.name,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontFamily = FontNunito.bold(),
                                    fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 2
                                )

                                // Date
                                Text(
                                    text = item.date,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                    fontFamily = FontNunito.medium(),
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                // Distance under date
                                if (item.distance.isNotEmpty()) {
                                    HtmlText(
                                        text = item.distance,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                        fontFamily = FontNunito.regular(),
                                        fontSize = 13.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                // Place and views
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Place
                                    if (item.place.isNotEmpty()) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.LocationOn,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp),
                                                tint = MaterialTheme.colorScheme.secondary
                                            )
                                            Text(
                                                text = item.place,
                                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                                fontFamily = FontNunito.regular(),
                                                fontSize = 12.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.weight(1f, fill = false)
                                            )
                                        }
                                    }

                                    // Views
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Visibility,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                        )
                                        Text(
                                            text = item.views.toString(),
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                            fontFamily = FontNunito.regular(),
                                            fontSize = 12.sp,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
