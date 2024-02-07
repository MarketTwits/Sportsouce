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
import com.markettwits.core_ui.theme.SportSouceColor
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
    //TODO(return check status code)
    if (distance.isNotEmpty()
    //   && startStatus.code == 3
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
                color = SportSouceColor.SportSouceBlue
            )
            Icon(
                imageVector = if (!panelState) Icons.AutoMirrored.Filled.KeyboardArrowRight else Icons.Default.KeyboardArrowDown,
                contentDescription = "",
                tint = SportSouceColor.SportSouceBlue
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
                color = SportSouceColor.SportSouceLighBlue,
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
                color = SportSouceColor.SportSouceBlue
            )
            HorizontalDivider()
            Text(
                text = "Осталось слотов : " + item.distance.slots,
                fontSize = 12.sp,
                fontFamily = FontNunito.bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = SportSouceColor.SportSouceBlue
            )
            if (paymentDisabled && paymentType.isNotEmpty()) {
                Text(
                    text = paymentType,
                    fontSize = 12.sp,
                    fontFamily = FontNunito.bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = SportSouceColor.SportSouceBlue
                )
            } else {
                Text(
                    text = "Цена : " + item.distance.price + " ₽",
                    fontSize = 12.sp,
                    fontFamily = FontNunito.bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = SportSouceColor.SportSouceBlue
                )
            }
            val enabled = (item.distance.slots.toInt()) > 0
            //TODO(return check status code)
            // if (enabled) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SportSouceColor.SportSouceLighBlue,
                    ),
                    onClick = { onClick() },
                ) {
                    Text("Зарегистрироваться")
                }
//            } else {
//                Button(
//                    enabled = false,
//                    colors = ButtonDefaults.buttonColors(
//                        disabledContainerColor = SportSouceColor.VeryLighBlue,
//                    ),
//                    onClick = { onClick() },
//                ) {
//
//                    Text("Слоты закончились")
//                }
//            }

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
//            .width(210.dp)
//            .height(150.dp)
            .border(
                width = 3.dp,
                color = SportSouceColor.SportSouceLighBlue,
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
                color = SportSouceColor.SportSouceBlue
            )
            Text(
                text = "",
                fontSize = 12.sp,
                fontFamily = FontNunito.bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = SportSouceColor.SportSouceBlue
            )
            if (paymentDisabled && paymentType.isNotEmpty()) {
                Text(
                    text = paymentType,
                    fontSize = 12.sp,
                    fontFamily = FontNunito.bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = SportSouceColor.SportSouceBlue
                )
            } else {
                Text(
                    text = "Цена : " + item.price + " ₽",
                    fontSize = 12.sp,
                    fontFamily = FontNunito.bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = SportSouceColor.SportSouceBlue
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SportSouceColor.SportSouceLighBlue,
                    ),
                    onClick = { onClick() },
                ) {
                    Text("Зарегистрироваться")
                }
            }
        }
    }
}