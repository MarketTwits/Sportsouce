package com.markettwits.sportsouce.start.presentation.result.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.LazyPagingItems
import com.markettwits.sportsouce.start.presentation.result.model.MemberResult

@Composable
internal fun StartMemberResultsPagedList(
    modifier: Modifier = Modifier,
    totalCount: Int,
    items: LazyPagingItems<MemberResult>,
    onClickMemberResult: (MemberResult) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (totalCount > 0) {
            item {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Всего $totalCount результатов",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                )
            }
        }

        items(count = items.itemCount) { index ->
            val item = items[index]
            if (item != null) {
                ResultCard(
                    result = item,
                    onClickMemberResult = onClickMemberResult,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(tween(300))
                )
            }
        }

        if (items.loadState.append is LoadStateLoading) {
            items(20) {
                ResultCardShimmer()
            }
        }
    }
}
