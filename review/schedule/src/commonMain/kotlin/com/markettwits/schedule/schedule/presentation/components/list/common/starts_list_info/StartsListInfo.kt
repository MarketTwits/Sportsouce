package com.markettwits.schedule.schedule.presentation.components.list.common.starts_list_info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.starts_common.domain.StartsListItem

@Composable
fun StartsListInfo(modifier: Modifier = Modifier, startsListItem: List<StartsListItem>) {
    var items by remember {
        mutableStateOf(emptyList<StartsListInfoItem>())
    }
    LaunchedEffect(key1 = startsListItem) {
        items = StartsListInfoMapperBase.map(startsListItem)
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach {
            StartsListInfoItemLabel(
                modifier = modifier,
                title = it.title,
                value = it.value
            )
        }
    }
}

@Composable
fun StartsListInfoItemLabel(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally

) {
    Column(
        modifier = modifier.padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = horizontalAlignment
    ) {
        Text(
            modifier = Modifier,
            text = value,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary,
            overflow = TextOverflow.Ellipsis,
            fontFamily = FontNunito.bold(),
            fontSize = 16.sp
        )
        Text(
            modifier = modifier,
            text = title,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = FontNunito.regular(),
            fontSize = 14.sp
        )
    }
}