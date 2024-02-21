package com.markettwits.review.presentation.components.actual

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
fun ActualStarts(
    modifier: Modifier = Modifier,
    starts: List<StartsListItem>,
    onClick: (Int) -> Unit
) {
    Text(
        modifier = modifier.padding(horizontal = 10.dp),
        text = "Актуальное",
        color = MaterialTheme.colorScheme.tertiary,
        fontFamily = FontNunito.bold,
        fontSize = 18.sp
    )
    Column {
        starts.take(5).forEach {
            StartCard(start = it, onItemClick = {
                onClick(it)
            })
        }
    }
}