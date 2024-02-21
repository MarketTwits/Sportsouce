package com.markettwits.registrations.registrations.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.registrations.registrations.domain.StartsStateInfo

@Composable
fun RegistrationsCard(
    start: StartsStateInfo,
    onItemClick: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(height = 200.dp)
            .padding(10.dp)
            .clickable {
                onItemClick(start.startId)
            }
    ) {
        Row {
            RegistrationsCardImageCard(
                modifier = Modifier,
                start.image,
                start.dateStart,
                start.payment,
                start.statusCode
            )
            RegistrationsCardInfoStatusInfo(
                title = start.startTitle,
                distance = start.distance,
                kindOfSport = start.kindOfSport,
                team = start.team,
                group = start.ageGroup,
                dateStart = start.dateStart,
                payment = start.payment,
                cost = start.cost
            )
        }
    }
}

@Composable
fun RegistrationsCardImageCard(
    modifier: Modifier = Modifier,
    image: String,
    date: String,
    payment: Boolean,
    status: StartStatus
) {

    Box(
        modifier = modifier
            .size(width = 120.dp, height = 170.dp)
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
                    painter = painterResource(id = com.markettwits.core_ui.R.drawable.default_start_image)
                )
            },
            success = {
                SubcomposeAsyncImageContent(modifier = modifier)
            }
        )
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            RegistrationsCardInfoStroke(date)
            RegistrationsCardInfoStatus(status, payment)
        }
    }
}

@Composable
private fun RegistrationsCardInfoStroke(title: String) {
    Row(
        modifier = Modifier
            .background(SportSouceColor.SportSouceLighBlue)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            color = Color.White,
            fontFamily = FontNunito.bold,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RegistrationsCardInfoStatus(status: StartStatus, payment: Boolean) {
    if (payment) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                .background(startStatusBackground(status.code))
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = status.name,
                color = Color.White,
                fontFamily = FontNunito.bold,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                .background(SportSouceColor.SportSouceLightRed)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Слот не оплачен",
                color = Color.White,
                fontFamily = FontNunito.bold,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun RegistrationsCardInfoStatusInfo(
    modifier: Modifier = Modifier,
    title: String,
    distance: String,
    kindOfSport: String,
    cost: String,
    team: String,
    group: String,
    dateStart: String,
    payment: Boolean
) {
    Column(modifier = modifier.padding(start = 10.dp, end = 10.dp)) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontFamily = FontNunito.bold,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = SportSouceColor.SportSouceBlue
        )
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontFamily = FontNunito.bold)) {
                    append("Дистанция: ")
                }
                withStyle(style = SpanStyle(fontFamily = FontNunito.regular)) {
                    append(distance)
                }
            },
            fontSize = 12.sp,
            overflow = TextOverflow.Ellipsis,
            fontFamily = FontNunito.medium,
            color = SportSouceColor.SportSouceBlue
        )
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontFamily = FontNunito.bold)) {
                    append("Команда: ")
                }
                withStyle(style = SpanStyle(fontFamily = FontNunito.regular)) {
                    append(team)
                }
            },
            fontSize = 12.sp,
            overflow = TextOverflow.Ellipsis,
            fontFamily = FontNunito.medium,
            color = SportSouceColor.SportSouceBlue
        )
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontFamily = FontNunito.bold)) {
                    append("Возрастная группа: ")
                }
                withStyle(style = SpanStyle(fontFamily = FontNunito.regular)) {
                    append(group)
                }
            },
            fontSize = 12.sp,
            overflow = TextOverflow.Ellipsis,
            fontFamily = FontNunito.medium,
            color = SportSouceColor.SportSouceBlue
        )
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontFamily = FontNunito.bold)) {
                    append("Цена: ")
                }
                withStyle(style = SpanStyle(fontFamily = FontNunito.regular)) {
                    append(cost + "₽")
                }
            },
            fontSize = 12.sp,
            overflow = TextOverflow.Ellipsis,
            fontFamily = FontNunito.medium,
            color = SportSouceColor.SportSouceBlue
        )
    }
}

fun startStatusBackground(statusCode: Int): Color =
    when (statusCode) {
        2 -> SportSouceColor.SportSouceRegistryCommingSoonYellow
        3 -> SportSouceColor.SportSouceRegistryOpenGreen
        4 -> SportSouceColor.SportSouceLightRed
        6 -> SportSouceColor.SportSouceStartEndedPink
        else -> Color.Blue
    }