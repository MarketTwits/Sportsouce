package com.markettwits.schedule.schedule.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.starts_common.domain.StartsListItem
import com.markettwits.starts_common.presentation.StartCard

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
            color = MaterialTheme.colorScheme.tertiary,
            fontFamily = FontNunito.bold,
            fontSize = 18.sp
        )
        if (starts.isNotEmpty()) {
            StartCard(
                modifier = Modifier.fillMaxSize(),
                start = starts[0],
                onItemClick = onClick::invoke
            )
        } else {
            Text(
                modifier = modifier.padding(horizontal = 10.dp),
                text = "Информация о стартах отсутствует",
                color = MaterialTheme.colorScheme.tertiary,
                fontFamily = FontNunito.medium,
                fontSize = 14.sp
            )
        }

    }

}