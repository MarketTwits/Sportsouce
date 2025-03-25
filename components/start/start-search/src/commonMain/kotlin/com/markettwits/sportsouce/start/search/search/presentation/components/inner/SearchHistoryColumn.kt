package com.markettwits.sportsouce.start.search.search.presentation.components.inner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun ColumnScope.SearchHistoryColumn(
    items: List<String>,
    onClick: (String) -> Unit
) {
    if (items.isNotEmpty()) {
        Text(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(10.dp),
            text = "НЕДАВНИЕ",
            color = MaterialTheme.colorScheme.outline,
            fontFamily = FontNunito.bold(),
            fontSize = 14.sp,
            overflow = TextOverflow.Visible
        )
    }
    items.forEach { item ->
        SearchHistoryItem(
            modifier = Modifier.align(Alignment.Start),
            value = item
        ) {
            onClick(it)
        }
    }
}

@Composable
private fun SearchHistoryItem(
    modifier: Modifier = Modifier,
    value: String,
    onClick: (String) -> Unit
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(14.dp)
        .clickable { onClick(value) }
    ) {
        Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = "time",
            tint = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = FontNunito.semiBoldBold(),
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}