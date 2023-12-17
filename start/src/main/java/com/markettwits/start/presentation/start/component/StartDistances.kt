package com.markettwits.start.presentation.start.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.components.Shapes
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.start.data.model.DistanceInfo

@Composable
fun StartDistances(modifier: Modifier = Modifier, distance: List<DistanceInfo>) {
    if (distance.isNotEmpty()){
        Column(modifier = modifier) {
            Text(
                text = "Дистанции",
                fontSize = 16.sp,
                fontFamily = FontNunito.bold,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = SportSouceColor.SportSouceBlue
            )
            LazyRow {
                items(distance) {
                    Column(modifier.clip(Shapes.medium)) {
                        DistanceItem(it)
                    }
                }
            }
        }
        HorizontalDivider()
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
        Column(modifier = Modifier
            .align(Alignment.Center)
            .padding(10.dp)) {
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