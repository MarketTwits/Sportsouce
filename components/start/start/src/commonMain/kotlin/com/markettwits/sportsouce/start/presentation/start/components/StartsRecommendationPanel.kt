package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

@Composable
internal fun StartsRecommendationPanel(
    modifier: Modifier = Modifier,
    items: List<StartsListItem>,
    onItemClick: (StartsListItem) -> Unit,
) {
    if (items.isNotEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Также рекомендуем",
                fontSize = 18.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items) { item ->
                    ImageCard(
                        item = item,
                        onClick = { onItemClick(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ImageCard(
    modifier: Modifier = Modifier,
    item: StartsListItem,
    onClick: () -> Unit,
) {
    OnBackgroundCard(
        modifier = modifier
            .height(300.dp)
            .width(250.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
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
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.4f)
                                ),
                                startY = 80f
                            )
                        )
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = item.date,
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = FontNunito.semiBoldBold(),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = item.name,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    fontFamily = FontNunito.bold(),
                    fontSize = 16.sp,
                    lineHeight = 20.sp
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    HtmlText(
                        text = item.distance.ifEmpty { item.description },
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                        fontFamily = FontNunito.regular(),
                        lineHeight = 16.sp,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.primaryContainer
                                    )
                                )
                            )
                    )
                }
            }
        }
    }
}
