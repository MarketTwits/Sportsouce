package com.markettwits.sportsouce.news.news_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.checkbox.FilterChipBase
import com.markettwits.sportsouce.news.common.model.NewsHashtag

@Composable
fun NewsSelectedHashtagsRow(
    selectedHashtags: List<NewsHashtag>,
    onRemoveHashtag: (NewsHashtag) -> Unit,
) {
    if (selectedHashtags.isEmpty()) return

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        items(selectedHashtags, key = { it.id }) { hashtag ->
            FilterChipBase(
                selected = true,
                onClick = { onRemoveHashtag(hashtag) },
                label = hashtag.name
            )
        }
    }
}
