package com.markettwits.start.presentation.start.components

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.core_ui.items.components.Shapes
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.start.presentation.common.OnClick
import com.markettwits.start.presentation.common.StartContentBasePanel

@Composable
fun StartDistances(
    modifier: Modifier = Modifier,
    distance: List<DistanceItem>,
    startStatus: StartStatus,
    paymentDisabled: Boolean,
    paymentType: String,
    regLink: String,
    onClick: (DistanceItem, Boolean, String) -> Unit,
    onClickUrl: (String) -> Unit
) {
    if (distance.isNotEmpty() && startStatus.code == 3) {
        StartContentBasePanel(modifier = modifier, label = "Дистанции") {
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                distance.forEach {
                    Column(modifier.clip(Shapes.medium)) {
                        when (it) {
                            is DistanceItem.DistanceCombo -> {
                                DistanceItemCombo(
                                    item = it,
                                    paymentDisabled = paymentDisabled,
                                    paymentType = paymentType,
                                    onClick = {
                                        onClick(it, paymentDisabled, paymentType)
                                    })
                            }

                            is DistanceItem.DistanceInfo -> {
                                DistanceItemBase(
                                    item = it,
                                    paymentDisabled = paymentDisabled,
                                    paymentType = paymentType,
                                    onClick = {
                                        onClick(it, paymentDisabled, paymentType)
                                    })
                            }
                        }
                    }
                }
            }

        }
    }
    if (regLink.isNotEmpty()) {
        StartContentBasePanel(modifier = modifier, label = "Дистанции") {
            ButtonContentBase(
                modifier = modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.onSecondary,
                title = "Перейти на сайт регистрации",
                onClick = {
                    onClickUrl(regLink)
                }
            )
        }
    }
}

@Composable
fun DistanceItemBase(
    item: DistanceItem.DistanceInfo,
    paymentDisabled: Boolean,
    paymentType: String,
    onClick: OnClick
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .border(
                width = 3.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = Shapes.medium
            )
            .clip(Shapes.medium)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(10.dp)
        ) {
            Text(
                text = item.value,
                fontSize = 12.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.tertiary
            )
            HorizontalDivider()
            val infinity = item.distance.infinitySlot ?: false
            val slots = if (item.distance.slots.toInt() <= 0) 0 else item.distance.slots.toInt()
            if (!infinity) {
                Text(
                    text = "Осталось слотов : $slots",
                    fontSize = 12.sp,
                    fontFamily = FontNunito.bold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            if (paymentDisabled && paymentType.isNotEmpty()) {
                Text(
                    text = paymentType,
                    fontSize = 12.sp,
                    fontFamily = FontNunito.bold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary
                )
            } else {
                Text(
                    text = "Цена : " + item.distance.price + " ₽",
                    fontSize = 12.sp,
                    fontFamily = FontNunito.bold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            val enabled = (item.distance.slots.toInt()) > 0 || infinity
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
            ) {
                val title = if (enabled) "Зарегистрироваться" else "Слоты закончились"
                val titleColor =
                    if (enabled) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.outline
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                    ),
                    onClick = { onClick() },
                ) {
                    Text(title, color = titleColor)
                }
            }
        }
    }
}

@Composable
fun DistanceItemCombo(
    item: DistanceItem.DistanceCombo,
    paymentDisabled: Boolean,
    paymentType: String,
    onClick: OnClick
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .border(
                width = 3.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = Shapes.medium
            )
            .clip(Shapes.medium)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(10.dp)
        ) {
            Text(
                text = item.value,
                fontSize = 12.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 3,
                overflow = TextOverflow.Visible,
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = "",
                fontSize = 12.sp,
                fontFamily = FontNunito.bold(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.tertiary
            )
            if (paymentDisabled && paymentType.isNotEmpty()) {
                Text(
                    text = paymentType,
                    fontSize = 12.sp,
                    fontFamily = FontNunito.bold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary
                )
            } else {
                Text(
                    text = "Цена : " + item.price + " ₽",
                    fontSize = 12.sp,
                    fontFamily = FontNunito.bold(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                    ),
                    onClick = { onClick() },
                ) {
                    Text("Зарегистрироваться", color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        }
    }
}