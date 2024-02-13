package com.markettwits.starts_common.presentation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import com.markettwits.starts_common.domain.StartsListItem

@Composable
fun StartsScreenContent(items: List<StartsListItem>, onClick: (Int) -> Unit) {
    LazyColumn {
        items(items, key = { it -> it.id }) {
            StartCard(start = it, onItemClick = onClick::invoke)
        }
    }
}