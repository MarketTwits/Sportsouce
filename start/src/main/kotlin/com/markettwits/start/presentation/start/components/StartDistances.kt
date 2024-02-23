package com.markettwits.start.presentation.start.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.cloud.ext_model.DistanceItem
import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.start.presentation.common.Animation
import com.markettwits.start.presentation.common.OnClick

@Composable
fun StartDistances(
    modifier: Modifier = Modifier,
    distance: List<DistanceItem>,
    startStatus: StartStatus,
    paymentDisabled: Boolean,
    paymentType: String,
    onClick: (DistanceItem, Boolean, String) -> Unit,
) {
    var panelState by rememberSaveable {
        mutableStateOf(true)
    }
    if (distance.isNotEmpty()
        && startStatus.code == 3
    ) {
        HorizontalDivider()
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    panelState = !panelState
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Дистанции",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.tertiary
            )
            Icon(
                imageVector = if (!panelState) Icons.AutoMirrored.Filled.KeyboardArrowRight else Icons.Default.KeyboardArrowDown,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
        androidx.compose.animation.AnimatedVisibility(
            visible = panelState,
            enter = Animation.enterAnimation(Animation.DEFAULT_TOP_BAR_ANIMATION_DURATION),
            exit = Animation.exitAnimation(Animation.DEFAULT_TOP_BAR_ANIMATION_DURATION),
        ) {
            LazyRow {
                items(distance) {
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
                fontFamily = FontNunito.bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.tertiary
            )
            HorizontalDivider()
            Text(
                text = "Осталось слотов : " + item.distance.slots,
                fontSize = 12.sp,
                fontFamily = FontNunito.bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.tertiary
            )
            if (paymentDisabled && paymentType.isNotEmpty()) {
                Text(
                    text = paymentType,
                    fontSize = 12.sp,
                    fontFamily = FontNunito.bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary
                )
            } else {
                Text(
                    text = "Цена : " + item.distance.price + " ₽",
                    fontSize = 12.sp,
                    fontFamily = FontNunito.bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            val enabled = (item.distance.slots.toInt()) > 0
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
            ) {

                val title = if (enabled) "Зарегистрироваться" else "Слоты закончились"
                val titleColor =
                    if (enabled) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.outline
                Button(
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
                fontFamily = FontNunito.bold,
                maxLines = 3,
                overflow = TextOverflow.Visible,
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = "",
                fontSize = 12.sp,
                fontFamily = FontNunito.bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.tertiary
            )
            if (paymentDisabled && paymentType.isNotEmpty()) {
                Text(
                    text = paymentType,
                    fontSize = 12.sp,
                    fontFamily = FontNunito.bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary
                )
            } else {
                Text(
                    text = "Цена : " + item.price + " ₽",
                    fontSize = 12.sp,
                    fontFamily = FontNunito.bold,
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
                    modifier = Modifier.align(Alignment.CenterVertically),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                    ),
                    onClick = { onClick() },
                ) {
                    Text("Зарегистрироваться")
                }
            }
        }
    }
}