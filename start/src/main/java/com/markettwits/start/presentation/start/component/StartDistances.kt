package com.markettwits.start.presentation.start.component

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import com.markettwits.cloud.model.common.StartStatus
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.data.model.DistanceInfo
import com.markettwits.start.presentation.common.Animation

@Composable
fun StartDistances(modifier: Modifier = Modifier, distance: List<DistanceInfo>, startStatus: StartStatus) {
    var panelState by rememberSaveable{
        mutableStateOf(true)
    }
    if (distance.isNotEmpty() && startStatus.code == 3) {
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
                    imageVector = if (!panelState) Icons.Default.KeyboardArrowRight else Icons.Default.KeyboardArrowDown,
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
                            DistanceItem(it)
                        }
                    }
                }
            }
        }
}

@Composable
fun DistanceItem(item: DistanceInfo) {

    Box(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .border(width = 3.dp, color = SportSouceColor.SportSouceLighBlue, shape = Shapes.medium)
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
            Text(
                text = "Цена : " + item.distance.price + " р.",
                fontSize = 12.sp,
                fontFamily = FontNunito.bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = SportSouceColor.SportSouceBlue
            )
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = SportSouceColor.SportSouceLighBlue),
                onClick = { /* TODO: обработать нажатие на кнопку */ },
            ) {
                Text("Зарегистрироваться")
            }
        }
    }
}