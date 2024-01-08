package com.markettwits.presentation.components.actual

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.markettwits.core_ui.theme.FontNunito
import com.markettwits.core_ui.theme.SportSouceColor
import com.markettwits.starts.StartsListItem
import com.markettwits.starts.components.success.StartCard

@Composable
fun ActualStarts(
    modifier : Modifier = Modifier,
    starts : List<StartsListItem>,
    onClick : (Int) -> Unit
) {
    Text(
        modifier = modifier.padding(horizontal = 10.dp),
        text = "Актуальное",
        color = SportSouceColor.SportSouceBlue,
        fontFamily = FontNunito.bold,
        fontSize = 18.sp
    )
    LazyColumn(
        modifier = modifier.height(1000.dp),
        userScrollEnabled = false,
    ) {
        items(starts) {
            StartCard(start = it){}
        }
    }
}