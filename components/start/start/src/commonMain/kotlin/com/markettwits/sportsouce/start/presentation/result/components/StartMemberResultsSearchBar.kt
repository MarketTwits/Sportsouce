package com.markettwits.sportsouce.start.presentation.result.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.sportsouce.start.presentation.common.components.StartListSearchBar

@Composable
fun StartMemberResultsSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onValueChange: (String) -> Unit,
    onClickGoBack: () -> Unit,
    onClickBrush: () -> Unit,
    filterCount: Int = 0,
    onFilter: (() -> Unit)? = null,
) {
    StartListSearchBar(query, onValueChange, onClickGoBack, onClickBrush, modifier, filterCount, onFilter)
}
