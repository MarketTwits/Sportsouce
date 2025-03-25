package com.markettwits.sportsouce.profile.registrations.detail.components.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.markettwits.sportsouce.profile.registrations.list.domain.StartOrderInfo

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
        Row(
            modifier = it
                .clip(Shapes.medium)
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
          //  verticalAlignment = Alignment.CenterVertically
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
                orderId = item.id,
                members = item.members
            )
        }
    }
}

@Composable
private fun RegistrationsCardInfoStatusInfo(
    modifier: Modifier = Modifier,
    orderId: Int,
    title: String,
    cost: String,
    members: List<StartOrderInfo.Member>,
) {
    Column(modifier = modifier.padding(start = 10.dp, end = 10.dp)) {
        Text(
            text = "№ $orderId",
            fontSize = 16.sp,
            fontFamily = FontNunito.bold(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = title,
            fontSize = 16.sp,
            fontFamily = FontNunito.bold(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.tertiary
        )

        RegistrationsCardInfoStatusInfoText(
            label = "Цена : ",
            value = "$cost ₽"
        )

        Column {
            members.forEachIndexed { index, member ->
                Spacer(modifier = Modifier.height(8.dp))
                RegistrationsCardMemberInfo(
                    index = index + 1,
                    fullName = member.surname  + " " + member.name,
                    group = member.ageGroupName,
                    distance = member.distanceName,
                    kindOfSport = member.formatName,
                    team = member.teamName,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

    }
}

@Composable
private fun RegistrationsCardMemberInfo(
    modifier: Modifier = Modifier,
    index : Int,
    fullName: String,
    group: String,
    team: String,
    distance: String,
    kindOfSport: String,
) {
    Column(modifier = modifier) {
        RegistrationsCardInfoStatusInfoText(
            modifier = Modifier.padding(start = 4.dp),
            label = "Участник : ",
            value = index.toString()
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
                    painter = DefaultImages.EmptyImageStart()
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
            withStyle(style = SpanStyle(fontFamily = FontNunito.bold())) {
                append(label)
            }
            withStyle(style = SpanStyle(fontFamily = FontNunito.medium())) {
                append(value)
            }
        },
        maxLines = 1,
        minLines = 1,
        fontSize = 12.sp,
        overflow = TextOverflow.Ellipsis,
        fontFamily = FontNunito.medium(),
        color = MaterialTheme.colorScheme.outline
    )
}