package com.markettwits.sportsouce.profile.registrations.presentation.detail.components.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        onClick = {
            onClickStart(item.startId)
        }
    ) {
        Row(
            modifier = it
                .clip(Shapes.medium),
            horizontalArrangement = Arrangement.Center,
        ) {
            RegistrationsCardImageCard(
                image = item.image
            )
            RegistrationsCardInfoStatusInfo(
                title = item.startTitle,
                startDate = item.dateStartPreview,
                orderId = item.id,
                paymentStatus = item.payment,
            )
        }
    }
}

@Composable
private fun RegistrationsCardInfoStatusInfo(
    modifier: Modifier = Modifier,
    paymentStatus: StartOrderPaymentStatus,
    orderId: Int,
    title: String,
    startDate: String,
) {
    Column(modifier = modifier.padding(start = 10.dp, end = 10.dp)) {
        Spacer(Modifier.height(4.dp))
        Text(
            modifier = modifier,
            text = startDate,
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            fontFamily = FontNunito.medium(),
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(Modifier.height(4.dp))
        Row {
            Text(
                text = "№ $orderId",
                fontSize = 14.sp,
                fontFamily = FontNunito.medium(),
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = paymentStatus.title,
                fontSize = 14.sp,
                fontFamily = FontNunito.medium(),
                overflow = TextOverflow.Ellipsis,
                color = mapOrderStatus(paymentStatus)
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontFamily = FontNunito.bold(),
            maxLines = 3,
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
            .size(width = 130.dp, height = 150.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        SubcomposeAsyncImage(
            model = image,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            error = {
                SubcomposeAsyncImageContent(
                    modifier = modifier,
                    painter = DefaultImages.EmptyImageStart()
                )
            },
            success = {
                SubcomposeAsyncImageContent(modifier = modifier)
            }
        )
    }
}