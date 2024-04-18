package com.markettwits.registrations.simple_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.image.DefaultImages
import com.markettwits.core_ui.items.image.imageRequestCrossfade
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.SportSouceColor
import com.markettwits.registrations.list.domain.StartOrderInfo


@Composable
fun StartOrderSimpleCard(
    modifier: Modifier = Modifier,
    start: StartOrderInfo,
    onItemClick: (StartOrderInfo) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(Shapes.medium)
            .clickable {
                onItemClick(start)
            }
    ) {
        Row {
            ImageCard(
                image = start.image,
            )
            ImageCardInfo(
                modifier = Modifier.padding(start = 10.dp),
                title = start.startTitle,
                status = start.statusCode,
                date = start.dateStartPreview,
                payment = start.payment,
                cost = start.cost,
                onClick = { onItemClick(start) }
            )
        }
    }
}

@Composable
private fun ImageCard(
    modifier: Modifier = Modifier,
    image: String,
) {
    Box(
        modifier = modifier
            .size(width = 70.dp, height = 90.dp)
            .clip(Shapes.medium)
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            SubcomposeAsyncImage(
                model = imageRequestCrossfade(image),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                error = {
                    if (image.isEmpty())
                        SubcomposeAsyncImageContent(
                            modifier = modifier,
                            painter = DefaultImages.EmptyImageStart()
                        )
                    else
                        Box(modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer))
                },
                loading = {
                    Box(modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer))
                },
                success = {
                    SubcomposeAsyncImageContent(modifier = modifier)
                }
            )
        }
    }
}

@Composable
private fun ImageCardInfo(
    modifier: Modifier = Modifier,
    title: String,
    status: StartStatus,
    payment: StartOrderInfo.PaymentStatus,
    cost: String,
    date: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
    ) {
        Text(
            modifier = modifier,
            text = title,
            color = MaterialTheme.colorScheme.tertiary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            lineHeight = 16.sp,
            fontFamily = FontNunito.bold(),
            fontSize = 14.sp,
            textAlign = TextAlign.Start
        )
        if (payment.payment) {
            Text(
                modifier = modifier,
                text = status.name,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.outline,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontFamily = FontNunito.medium(),
                fontSize = 12.sp,
                textAlign = TextAlign.Start
            )
        } else {
            Text(
                modifier = modifier,
                text = "Слот не оплачен",
                lineHeight = 14.sp,
                color = SportSouceColor.SportSouceLightRed,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontFamily = FontNunito.bold(),
                fontSize = 12.sp,
                textAlign = TextAlign.Start
            )
            Button(
                shape = Shapes.medium,
                modifier = modifier,
                onClick = { onClick() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                contentPadding = PaddingValues(4.dp)
            ) {
                Text(
                    text = "Оплатить $cost ₽",
                    color = MaterialTheme.colorScheme.onSecondary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontFamily = FontNunito.bold(),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}