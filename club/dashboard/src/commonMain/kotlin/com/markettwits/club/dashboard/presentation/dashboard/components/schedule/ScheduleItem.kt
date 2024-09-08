package com.markettwits.club.dashboard.presentation.dashboard.components.schedule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.club.info.domain.models.Schedule
import com.markettwits.core_ui.items.components.OnBackgroundCard
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.text.HtmlText
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun ScheduleItem(
    modifier: Modifier = Modifier,
    schedule: Schedule,
    onClick: (Schedule) -> Unit
) {
    OnBackgroundCard(
        modifier = modifier.widthIn(min = 200.dp, max = 350.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
        ) {
            ScheduleItemRowInfo(
                title = "Дата и время",
                value = "${schedule.weekday} ${schedule.startDate}"
            )
            ScheduleItemRowInfo(
                title = "Вид тренировки",
                value = schedule.kindOfSport
            )
            ScheduleItemRowInfo(
                title = "Место проведения",
                value = schedule.address
            )
            if (schedule.trainerFullName.isNotEmpty()) {
                ScheduleItemRowInfo(
                    title = "Тренер",
                    value = schedule.trainerFullName
                )
            }
            ScheduleItemRowInfo(
                title = "Описание",
                value = schedule.description,
                isHtml = true
            )
        }
        ScheduleSubscriptionButton(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                onClick(schedule)
            }
        )
    }
}

@Composable
private fun ScheduleItemRowInfo(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    isHtml: Boolean = false,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier,
            textAlign = TextAlign.Start,
            text = "$title:",
            fontSize = 14.sp,
            overflow = TextOverflow.Ellipsis,
            fontFamily = FontNunito.semiBoldBold(),
            color = MaterialTheme.colorScheme.onPrimary
        )
        (if (isHtml) {
            HtmlText(
                modifier = Modifier,
                textAlign = TextAlign.Start,
                text = value,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                lineHeight = 12.sp,
                fontFamily = FontNunito.semiBoldBold(),
                color = MaterialTheme.colorScheme.outline
            )
        } else {
            Text(
                modifier = Modifier,
                textAlign = TextAlign.Start,
                text = value,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                fontFamily = FontNunito.semiBoldBold(),
                color = MaterialTheme.colorScheme.outline
            )
        })
    }
}

@Composable
private fun ScheduleSubscriptionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ButtonContentBase(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.secondary,
        textColor = Color.White,
        showContent = true,
        onClick = onClick,
        shape = Shapes.medium,
        content = {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                textAlign = TextAlign.Center,
                text = "Записаться на пробную",
                fontSize = 12.sp,
                fontFamily = FontNunito.bold(),
                color = Color.White
            )
        },
    )
}