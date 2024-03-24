package com.markettwits.schedule.schedule.presentation.components.detail.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.starts_common.domain.StartsListItem
import com.markettwits.starts_common.presentation.StartCard

@Composable
fun StartDetailScheduleContent(
    modifier: Modifier = Modifier,
    starts: List<StartsListItem>,
    onClick: (Int) -> Unit
) {
    LazyColumn {
        items(items = starts, key = { it -> it.id }) { start ->
            StartCard(modifier = modifier, start = start) {
                onClick(it)
            }
        }
    }
}