package com.markettwits.schedule.schedule.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.starts.components.StartCard
import com.markettwits.starts.presentation.StartsListItem

@Composable
fun ScheduleStartsRow(
    modifier: Modifier = Modifier,
    starts: List<StartsListItem>,
    day: String,
    onClick: (Int) -> Unit
) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(10.dp)) {
        HorizontalDivider(modifier = Modifier.padding(10.dp))
        Text(
            modifier = modifier.padding(horizontal = 10.dp),
            text = day,
            color = SportSouceColor.SportSouceBlue,
            fontFamily = FontNunito.bold,
            fontSize = 18.sp
        )
        if (starts.isNotEmpty()) {
            StartCard(
                modifier = Modifier.fillMaxSize(),
                start = starts[0],
                onItemClick = onClick::invoke
            )

            //TODO add LazyRow for starts
//            LazyRow(modifier = modifier.fillMaxSize()) {
//                items(starts) {
//                    StartCard(modifier = Modifier.fillMaxSize(), start = it, onItemClick = onClick::invoke)
//                }
//            }
        } else {
            Text(
                modifier = modifier.padding(horizontal = 10.dp),
                text = "Информация о стартах отсутствует",
                color = SportSouceColor.SportSouceBlue,
                fontFamily = FontNunito.medium,
                fontSize = 14.sp
            )
        }

    }

}