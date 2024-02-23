package com.markettwits.registrations.start_order_profile.components.start

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.OnBackgroundCard
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.registrations.registrations.domain.StartOrderInfo
import com.markettwits.registrations.registrations.presentation.components.RegistrationsCardImageCard

@Composable
fun OrderStartCard(
    modifier: Modifier = Modifier,
    item: StartOrderInfo,
    onClickPayment: (Int) -> Unit
) {
    OnBackgroundCard(modifier = modifier) {
        Row(modifier = it.padding(10.dp)) {
            RegistrationsCardImageCard(
                modifier = Modifier,
                image = item.image
            )
            RegistrationsCardInfoStatusInfo(
                startId = item.startId,
                title = item.startTitle,
                cost = item.cost,
                date = item.dateStart,
                fullName = item.member,
                group = item.ageGroup,
                kindOfSport = item.kindOfSport,
                orderId = item.id,
                team = item.team,
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
    kindOfSport: String,
    orderId: Int,
    startId: Int,
    cost: String,
) {
    Column(modifier = modifier.padding(start = 10.dp, end = 10.dp)) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontFamily = FontNunito.bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.tertiary
        )
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