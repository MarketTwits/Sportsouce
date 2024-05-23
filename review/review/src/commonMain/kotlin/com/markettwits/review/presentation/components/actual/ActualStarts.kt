package com.markettwits.review.presentation.components.actual

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.window.rememberScreenSizeInfo
import com.markettwits.starts_common.domain.StartsListItem
import com.markettwits.starts_common.presentation.StartCard

@Composable
fun ActualStarts(
    modifier: Modifier = Modifier,
    starts: List<StartsListItem>,
    onClick: (Int) -> Unit
) {
    Text(
        modifier = modifier.padding(horizontal = 10.dp),
        text = "Актуальное",
        color = MaterialTheme.colorScheme.tertiary,
        fontFamily = FontNunito.bold(),
        fontSize = 18.sp
    )
    val oneColumns = rememberScreenSizeInfo().isPortrait()
    LazyVerticalGrid(
        modifier = modifier.height(900.dp),
        userScrollEnabled = false,
        columns = GridCells.Fixed(if (oneColumns) 1 else 2)
    ) {
        items(starts.take(if (oneColumns) 5 else 10)) {
            StartCard(start = it, onItemClick = {
                onClick(it)
            })
        }
    }
}