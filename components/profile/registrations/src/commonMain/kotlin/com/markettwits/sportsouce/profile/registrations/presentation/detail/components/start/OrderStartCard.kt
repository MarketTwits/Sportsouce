package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import com.markettwits.core_ui.items.components.progress.shimmer
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderInfo
import com.markettwits.sportsouce.profile.registrations.domain.StartOrderPaymentStatus

@Composable
fun OrderStartCard(
    modifier: Modifier = Modifier,
    item: StartOrderInfo,
    onClickStart: (Int) -> Unit,
) {
    OnBackgroundCard(
        modifier = modifier,
        onClick = {
            onClickStart(item.startId)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            RegistrationsCardImageCard(
                image = item.image,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OrderStatusRow(
                    modifier = Modifier.fillMaxWidth(),
                    paymentStatus = item.payment,
                    hasResults = item.members.any { it.results.isNotEmpty() }
                )

                Text(
                    text = item.startTitle,
                    fontSize = 16.sp,
                    fontFamily = FontNunito.bold(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    OrderInfoRow(
                        title = "Дата:",
                        value = item.dateStartPreview
                    )
                    OrderInfoRow(
                        title = "№ заказа:",
                        value = "${item.id}"
                    )
                    OrderInfoRow(
                        title = "Участников:",
                        value = "${item.members.size}"
                    )
                    if (item.cost.isNotEmpty() && item.cost != "0") {
                        OrderInfoRow(
                            title = "Стоимость:",
                            value = "${item.cost} ₽"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderStatusRow(
    modifier: Modifier = Modifier,
    paymentStatus: StartOrderPaymentStatus,
    hasResults: Boolean,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            shape = Shapes.small,
            colors = CardDefaults.cardColors(
                containerColor = mapOrderStatusColor(paymentStatus).copy(alpha = 0.15f)
            )
        ) {
            Text(
                text = paymentStatus.title,
                fontSize = 10.sp,
                fontFamily = FontNunito.semiBoldBold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = mapOrderStatusColor(paymentStatus),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
        if (hasResults) {
            Card(
                shape = Shapes.small,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                )
            ) {
                Text(
                    text = "Результаты",
                    fontSize = 10.sp,
                    fontFamily = FontNunito.semiBoldBold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun OrderInfoRow(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontFamily = FontNunito.medium(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontFamily = FontNunito.semiBoldBold(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}


@Composable
fun RegistrationsCardImageCard(
    modifier: Modifier = Modifier,
    image: String,
) {
    SubcomposeAsyncImage(
        model = imageRequestCrossfade(image.takeIf { it.isNotEmpty() }),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmer(
                        gradientColors = listOf(
                            androidx.compose.ui.graphics.Color.Transparent,
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                            androidx.compose.ui.graphics.Color.Transparent,
                        ),
                        tiltAngle = 30
                    )
            )
        },
        error = {
            SubcomposeAsyncImageContent(
                modifier = Modifier.fillMaxSize(),
                painter = DefaultImages.EmptyImageStart()
            )
        },
        success = {
            SubcomposeAsyncImageContent(modifier = Modifier.fillMaxSize())
        }
    )
}