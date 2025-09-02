package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.extensions.noRippleClickable
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo

@Composable
fun OrderDetailCard(
    modifier: Modifier = Modifier,
    orderInfo: StartOrderInfo,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(Shapes.large)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.95f)
                    )
                )
            )
            .padding(20.dp)
            .noRippleClickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header with image and basic info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Event image
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(Shapes.medium)
                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            ) {
                SubcomposeAsyncImage(
                    model = orderInfo.image.takeIf { it.isNotEmpty() },
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(Shapes.medium),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
                                .shimmer(
                                    tiltAngle = 30,
                                    gradientColors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                                        Color.Transparent,
                                    )
                                )
                        )
                    },
                    error = {
                        SubcomposeAsyncImageContent(
                            modifier = Modifier.fillMaxSize(),
                            painter = DefaultImages.EmptyImageStart()
                        )
                    }
                )
            }

            // Event title and basic info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = orderInfo.name,
                    fontSize = 18.sp,
                    fontFamily = FontNunito.bold(),
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = orderInfo.startTitle,
                    fontSize = 14.sp,
                    fontFamily = FontNunito.medium(),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Detailed information cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DetailInfoCard(
                icon = Icons.Default.CalendarToday,
                title = "Дата старта",
                value = orderInfo.dateStartPreview,
                modifier = Modifier.weight(1f)
            )
            DetailInfoCard(
                icon = Icons.Default.MonetizationOn,
                title = "Стоимость",
                value = if (orderInfo.cost.isNotEmpty()) "${orderInfo.cost} ₽" else "Бесплатно",
                modifier = Modifier.weight(1f)
            )
        }

        // Order details
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DetailRow(
                label = "ID заказа:",
                value = "#${orderInfo.id}"
            )
            DetailRow(
                label = "ID старта:",
                value = "#${orderInfo.startId}"
            )
        }
    }
}

@Composable
private fun DetailInfoCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    value: String,
) {
    Column(
        modifier = modifier
            .clip(Shapes.medium)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.outlineVariant,
                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.8f)
                    )
                )
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    CircleShape
                )
                .padding(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(20.dp)
            )
        }

        Text(
            text = title,
            fontSize = 12.sp,
            fontFamily = FontNunito.medium(),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = value,
            fontSize = 14.sp,
            fontFamily = FontNunito.semiBoldBold(),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun DetailRow(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontFamily = FontNunito.medium(),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontFamily = FontNunito.semiBoldBold(),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}