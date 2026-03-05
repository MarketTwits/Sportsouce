package com.markettwits.sportsouce.news.news_list.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NewsFeedLoadingList() {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val horizontalPadding = if (maxWidth < 380.dp) 12.dp else 16.dp
        val verticalSpacing = if (maxWidth < 380.dp) 10.dp else 12.dp
        val columns = if (maxWidth >= 680.dp) 2 else 1

        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(columns),
            verticalArrangement = Arrangement.spacedBy(verticalSpacing),
            horizontalArrangement = Arrangement.spacedBy(verticalSpacing),
            contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = 12.dp)
        ) {
            items(8) {
                NewsFeedItemShimmer()
            }

            item(span = { GridItemSpan(columns) }) {
                Spacer(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .height(2.dp)
                )
            }
        }
    }
}
