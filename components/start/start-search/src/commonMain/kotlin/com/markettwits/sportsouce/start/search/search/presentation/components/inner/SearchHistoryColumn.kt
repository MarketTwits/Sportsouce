package com.markettwits.sportsouce.start.search.search.presentation.components.inner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
fun SearchHistoryColumn(
    modifier: Modifier = Modifier,
    items: List<String>,
    onClick: (String) -> Unit,
    onDelete: (String) -> Unit,
) {
    Column(
        modifier
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
                value = item,
                onClick = { onClick(it) },
                onDelete = { onDelete(it) }
            )
        }
    }
}

@Composable
private fun SearchHistoryItem(
    modifier: Modifier = Modifier,
    value: String,
    onClick: (String) -> Unit,
    onDelete: (String) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick(value) },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = "time",
            tint = MaterialTheme.colorScheme.tertiary
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = FontNunito.semiBoldBold(),
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = { onDelete(value) }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "delete",
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}