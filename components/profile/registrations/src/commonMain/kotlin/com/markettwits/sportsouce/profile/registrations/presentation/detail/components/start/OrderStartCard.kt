package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start

import androidx.compose.foundation.layout.*
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
import com.markettwits.core_ui.items.image.DefaultImages
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
        Column {
            // Image at the very top (edge-to-edge)
            RegistrationsCardImageCard(
                image = item.image,
                modifier = Modifier.fillMaxWidth()
            )

            Column(modifier = Modifier.padding(10.dp)) {
                // Status row with subtle payment status color indication
                OrderStatusRow(
                    modifier = Modifier.fillMaxWidth(),
                    paymentStatus = item.payment,
                    hasResults = item.members.any { it.results.isNotEmpty() }
                )

                // Title with additional padding
                Text(
                    text = item.startTitle,
                    fontSize = 16.sp,
                    fontFamily = FontNunito.bold(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(top = 6.dp, bottom = 4.dp)
                )

                // Info rows
                OrderInfoRow(
                    title = "Дата старта",
                    value = item.dateStartPreview
                )
                OrderInfoRow(
                    title = "Номер заказа",
                    value = "№ ${item.id}"
                )
                OrderInfoRow(
                    title = "Участников",
                    value = "${item.members.size} чел."
                )
                if (item.cost.isNotEmpty() && item.cost != "0") {
                    OrderInfoRow(
                        title = "Стоимость",
                        value = "${item.cost} ₽"
                    )
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
        modifier = modifier.padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            shape = Shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = mapOrderStatusColor(paymentStatus).copy(alpha = 0.2f)
            )
        ) {
            Text(
                text = paymentStatus.title,
                fontSize = 12.sp,
                fontFamily = FontNunito.medium(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = mapOrderStatusColor(paymentStatus),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
        if (hasResults) {
            Card(
                shape = Shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                )
            ) {
                Text(
                    text = "Есть результаты",
                    fontSize = 12.sp,
                    fontFamily = FontNunito.medium(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
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
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontFamily = FontNunito.semiBoldBold(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontFamily = FontNunito.bold(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}


@Composable
fun RegistrationsCardImageCard(
    modifier: Modifier = Modifier,
    image: String,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(Shapes.medium)
    ) {
        SubcomposeAsyncImage(
            model = image,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
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
}