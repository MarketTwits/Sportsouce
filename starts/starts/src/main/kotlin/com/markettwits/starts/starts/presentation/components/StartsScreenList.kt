package com.markettwits.starts.starts.presentation.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.base_screen.FailedScreen
import com.markettwits.core_ui.items.base_screen.LoadingFullScreen
import com.markettwits.starts.starts.presentation.component.StartsUiState
import com.markettwits.starts_common.presentation.StartCard


@Composable
internal fun StartsScreenList(
    modifier: Modifier = Modifier,
    state: StartsUiState,
    page: Int,
    onClickItem: (Int) -> Unit,
    onClickRetry: () -> Unit
) {

    LazyColumn(modifier = modifier) {
        when (state) {
            is StartsUiState.Success -> {
                val items = state.items[page]
                items(items, key = { it.id }) {
                    StartCard(
                        start = it,
                        onItemClick = onClickItem
                    )
                }
            }

            is StartsUiState.Failed -> {
                item {
                    FailedScreen(
                        message = (state).message,
                        onClickRetry = {
                            onClickRetry()
                        })
                }
            }

            is StartsUiState.Loading -> {
                item {
                    LoadingFullScreen()
                }
            }
        }
    }
}