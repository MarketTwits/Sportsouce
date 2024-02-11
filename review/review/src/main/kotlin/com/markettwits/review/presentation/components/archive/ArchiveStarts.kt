package com.markettwits.review.presentation.components.archive

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.review.presentation.components.actual.StartCardSimple
import com.markettwits.starts_common.domain.StartsListItem

@Composable
fun ArchiveStarts(
    modifier: Modifier = Modifier, starts: List<StartsListItem>,
    onClick: (Int) -> Unit
) {
    Text(
        modifier = modifier.padding(horizontal = 10.dp),
        text = "Архив",
        color = SportSouceColor.SportSouceBlue,
        fontFamily = FontNunito.bold,
        fontSize = 18.sp
    )
    LazyRow(modifier = modifier) {
        items(starts) {
            StartCardSimple(start = it, onItemClick = {
                onClick(it)
            })
        }
    }
}