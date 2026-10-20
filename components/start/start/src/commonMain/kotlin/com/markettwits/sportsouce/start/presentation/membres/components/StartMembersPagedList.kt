package com.markettwits.sportsouce.start.presentation.membres.components

import androidx.compose.foundation.layout.*
import com.markettwits.core_ui.items.components.navigation.SauceLazyColumn as LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.markettwits.core_ui.items.theme.SauceTheme
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
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp,
            bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 16.dp)
    ) {
        if (totalCount > 0)
            item {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    text = "Участников: $totalCount",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SauceTheme.colors.textSecondary,
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

    }
}
