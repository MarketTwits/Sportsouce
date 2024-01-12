package com.markettwits.starts.presentation

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.markettwits.starts.components.StartCard

@Composable
fun StartsScreenContent(items: List<StartsListItem>, onClick: (Int) -> Unit) {
    LazyColumn {
        items(items) {
            StartCard(start = it, onItemClick = onClick::invoke)
        }
    }
}