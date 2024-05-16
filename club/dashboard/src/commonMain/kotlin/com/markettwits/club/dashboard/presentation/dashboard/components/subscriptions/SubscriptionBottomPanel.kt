package com.markettwits.club.dashboard.presentation.dashboard.components.subscriptions

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun SubscriptionBottomPanel(
    modifier: Modifier = Modifier,
    cost: String,
    countOfMonth: Int,
    onClickIncrease: () -> Unit,
    onClickDecrease: () -> Unit,
    onClickSubscribe: () -> Unit
) {
    Column(modifier = modifier.background(MaterialTheme.colorScheme.primary)) {
        Row(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
        ) {
            SubscriptionBottomPanelButton(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(0.6f),
                cost = cost,
                countOfMonth = countOfMonth,
                onClick = onClickSubscribe::invoke
            )
            Spacer(modifier = Modifier.padding(4.dp))
            SubscriptionCounter(
                Modifier
                    .height(60.dp)
                    .fillMaxWidth(1f),
                monthOfCount = countOfMonth,
                onClickDecrease = onClickDecrease::invoke,
                onClickIncrease = onClickIncrease::invoke
            )
        }

    }
}

@Composable
private fun SubscriptionBottomPanelButton(
    modifier: Modifier = Modifier,
    cost: String,
    countOfMonth: Int,
    onClick: () -> Unit
) {
    ButtonContentBase(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.secondary,
        textColor = Color.White,
        showContent = true,
        onClick = {
            onClick()
        },
        content = {
            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Записаться",
                    fontSize = 18.sp,
                    fontFamily = FontNunito.bold(),
                    color = Color.White
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "$countOfMonth месяц за $cost RUB",
                    fontSize = 12.sp,
                    fontFamily = FontNunito.regular(),
                    color = Color.White
                )
            }
        },
        shape = Shapes.medium
    )
}

@Composable
private fun SubscriptionCounter(
    modifier: Modifier = Modifier,
    monthOfCount: Int,
    onClickIncrease: () -> Unit,
    onClickDecrease: () -> Unit
) {

    Row(
        modifier = modifier
            .clip(Shapes.medium)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
                .clickable(onClick = onClickDecrease)
        ) {
            Text(
                modifier = Modifier
                    .offset(y = (-7).dp)
                    .padding(10.dp)
                    .align(Alignment.Center)
                    .animateContentSize(),
                text = "–",
                fontSize = 38.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Text(
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            text = monthOfCount.toString(),
            fontSize = 20.sp,
            fontFamily = FontNunito.bold(),
            color = MaterialTheme.colorScheme.tertiary
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .clip(RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                .clickable(onClick = onClickIncrease)
        ) {
            Text(
                modifier = Modifier
                    .offset(y = (-7).dp)
                    .padding(10.dp)
                    .align(Alignment.Center)
                    .animateContentSize(),
                text = "+",
                fontSize = 38.sp,
                fontFamily = FontNunito.bold(),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}