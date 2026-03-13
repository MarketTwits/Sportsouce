package com.markettwits.sportsouce.start.presentation.membres.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.LazyPagingItems
import com.markettwits.sportsouce.start.presentation.membres.models.StartMembersUi

@Composable
internal fun StartMembersPagedList(
    modifier: Modifier = Modifier,
    totalCount: Int,
    items: LazyPagingItems<StartMembersUi>,
    itemContent: @Composable (StartMembersUi) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(12.dp)
    ) {
        if (totalCount > 0)
            item {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Всего $totalCount результатов",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                )
            }
        items(count = items.itemCount) { index ->
            val item = items[index]
            if (item != null) {
                itemContent(item)

            }
        }
        if (items.loadState.append is LoadStateLoading) {
            items(20) {
                StartMemberCardShimmer()
            }
        }
        item {
            Spacer(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .height(2.dp)
            )
        }
    }
}
