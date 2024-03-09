package com.markettwits.start_search.search.presentation.components.inner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito

@Composable
fun SearchHistoryColumn(
    modifier: Modifier = Modifier,
    items: List<String>,
    onClick: (String) -> Unit
) {
    Column(modifier = modifier) {
        items.forEach { item ->
            SearchHistoryItem(value = item) {
                onClick(it)
            }
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
        .padding(10.dp)
        .clickable { onClick(value) }
    ) {
        Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = "time",
            tint = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.padding(horizontal = 5.dp))
        Text(
            text = value,
            color = MaterialTheme.colorScheme.outline,
            fontFamily = FontNunito.bold,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}