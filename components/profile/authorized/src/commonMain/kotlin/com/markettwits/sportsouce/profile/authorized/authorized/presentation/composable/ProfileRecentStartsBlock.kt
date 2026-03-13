package com.markettwits.sportsouce.profile.authorized.authorized.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.common.presentation.startKindOfSportsColor
import com.markettwits.sportsouce.starts.common.presentation.startKindOfSportsIcon
import com.markettwits.sportsouce.starts.common.presentation.startStatusBackground
import com.markettwits.sportsouce.starts.common.presentation.startStatusCompactMessage

@Composable
internal fun ProfileRecentStartsBlock(
    modifier: Modifier = Modifier,
    starts: List<StartsListItem>,
    onClickStart: (StartsListItem) -> Unit,
) {
    if (starts.isEmpty()) return

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            text = "Вы смотрели",
            fontSize = 18.sp,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
        ) {
            starts.forEach { start ->
                RecentStartCard(start = start, onClickStart = onClickStart)
                Spacer(modifier = Modifier.width(10.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun RecentStartCard(
    start: StartsListItem,
    onClickStart: (StartsListItem) -> Unit,
) {
    Card(
        modifier = Modifier
            .width(332.dp)
            .height(172.dp),
        onClick = { onClickStart(start) },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 0.dp, start = 0.dp, end = 8.dp, bottom = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(118.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12.dp))
            ) {
                SubcomposeAsyncImage(
                    model = imageRequestCrossfade(start.image),
                    contentDescription = start.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize(),
                    error = {
                        if (start.image.isEmpty()) {
                            SubcomposeAsyncImageContent(painter = DefaultImages.EmptyImageStart())
                        } else {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                            )
                        }
                    },
                    loading = {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                        )
                    },
                    success = {
                        SubcomposeAsyncImageContent()
                    }
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(startStatusBackground(start.statusCode.id).copy(alpha = 0.8f))
                ) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        text = startStatusCompactMessage(start.statusCode.id),
                        fontFamily = FontNunito.bold(),
                        fontSize = 10.sp,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.padding(end = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = start.name,
                        fontFamily = FontNunito.bold(),
                        fontSize = 15.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = start.date,
                        fontFamily = FontNunito.medium(),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (start.place.isNotBlank()) {
                        Text(
                            text = start.place,
                            fontFamily = FontNunito.regular(),
                            fontSize = 12.sp,
                            lineHeight = 15.sp,
                            color = MaterialTheme.colorScheme.outline,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if (start.kindOfSports.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier.padding(end = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        start.kindOfSports.take(2).forEach { sport ->
                            val sportColor = startKindOfSportsColor(sport)
                            AssistChip(
                                onClick = { onClickStart(start) },
                                label = {
                                    Text(
                                        text = sport.name,
                                        fontFamily = FontNunito.medium(),
                                        fontSize = 11.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = startKindOfSportsIcon(sport),
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = sportColor.copy(alpha = 0.14f),
                                    labelColor = sportColor,
                                    leadingIconContentColor = sportColor
                                ),
                                border = null
                            )
                        }
                        if (start.kindOfSports.size > 2) {
                            Text(
                                text = "+${start.kindOfSports.size - 2}",
                                fontFamily = FontNunito.bold(),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
