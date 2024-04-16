package com.markettwits.schedule.schedule.presentation.components.detail.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.starts_common.domain.StartsListItem
import com.markettwits.starts_common.presentation.StartCard

@Composable
fun StartDetailScheduleContent(
    modifier: Modifier = Modifier,
    starts: List<StartsListItem>,
    onClick: (Int) -> Unit
) {
    LazyColumn(modifier = modifier.padding(10.dp)) {
        itemsIndexed(items = starts) { index, item ->
            Row(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.wrapContentHeight(),
                    text = (index + 1).toString(),
                    color = MaterialTheme.colorScheme.tertiary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontFamily = FontNunito.bold(),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.padding(5.dp))
                StartCard(start = item) {
                    onClick(it)
                }
            }
        }
    }
    Spacer(modifier = Modifier.padding(10.dp))
}