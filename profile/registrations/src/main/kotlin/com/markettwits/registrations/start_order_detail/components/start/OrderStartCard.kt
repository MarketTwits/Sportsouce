package com.markettwits.registrations.start_order_detail.components.start

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.markettwits.core_ui.R
import com.markettwits.core_ui.components.OnBackgroundCard
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.registrations.registrations_list.domain.StartOrderInfo

@Composable
fun OrderStartCard(
    modifier: Modifier = Modifier,
    item: StartOrderInfo,
    onClickStart: (Int) -> Unit,
) {
    OnBackgroundCard(modifier = modifier.clickable {
        onClickStart(item.startId)
    }) {
        Row(
            modifier = it.padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.width(width = 130.dp)) {
                RegistrationsCardImageCard(
                    modifier = Modifier,
                    image = item.image,
                )
                OrderPaymentStatus(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    payment = item.payment
                )
            }
            RegistrationsCardInfoStatusInfo(
                title = item.startTitle,
                cost = item.cost,
                date = item.dateStart,
                fullName = item.member,
                group = item.ageGroup,
                kindOfSport = item.kindOfSport,
                orderId = item.id,
                paymentStatus = item.payment,
                team = item.team,
                distance = item.distance,
            )
        }
    }
}

@Composable
private fun RegistrationsCardInfoStatusInfo(
    modifier: Modifier = Modifier,
    title: String,
    date: String,
    fullName: String,
    group: String,
    team: String,
    distance: String,
    kindOfSport: String,
    orderId: Int,
    paymentStatus: StartOrderInfo.PaymentStatus,
    cost: String,
) {
    Column(modifier = modifier.padding(start = 10.dp, end = 10.dp)) {
        Text(
            text = "№ $orderId",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = title,
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.tertiary
        )
        //   OrderPaymentStatus(modifier = Modifier.padding(vertical = 5.dp), payment = paymentStatus)
        RegistrationsCardInfoStatusInfoText(
            label = "Дата: ",
            value = date
        )
        RegistrationsCardInfoStatusInfoText(
            label = "ФИО : ",
            value = fullName
        )
        RegistrationsCardInfoStatusInfoText(
            label = "Группа : ",
            value = group
        )
        RegistrationsCardInfoStatusInfoText(
            label = "Дистанция : ",
            value = distance
        )
        RegistrationsCardInfoStatusInfoText(
            label = "Команда : ",
            value = team
        )
        RegistrationsCardInfoStatusInfoText(
            label = "Формат : ",
            value = kindOfSport
        )
        RegistrationsCardInfoStatusInfoText(
            label = "Цена : ",
            value = "$cost ₽"
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
            .size(width = 130.dp, height = 190.dp)
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
                    painter = painterResource(id = R.drawable.default_start_image)
                )
            },
            success = {
                SubcomposeAsyncImageContent(modifier = modifier)
            }
        )
    }
}

@Composable
private fun RegistrationsCardInfoStatusInfoText(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontFamily = FontNunito.bold)) {
                append(label)
            }
            withStyle(style = SpanStyle(fontFamily = FontNunito.medium)) {
                append(value)
            }
        },
        maxLines = 1,
        minLines = 1,
        fontSize = 12.sp,
        overflow = TextOverflow.Ellipsis,
        fontFamily = FontNunito.medium,
        color = MaterialTheme.colorScheme.outline
    )
}