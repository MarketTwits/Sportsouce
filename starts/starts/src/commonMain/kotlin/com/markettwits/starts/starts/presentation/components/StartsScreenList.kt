package com.markettwits.starts.starts.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.items.base_screen.FailedScreen
import com.markettwits.core_ui.items.base_screen.LoadingFullScreen
import com.markettwits.core_ui.items.window.rememberScreenSizeInfo
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
    val size = rememberScreenSizeInfo()

    when (state) {

        is StartsUiState.Success -> {
            LazyVerticalGrid(
                modifier = modifier.fillMaxSize(),
                columns = GridCells.Fixed(if (size.isPortrait()) 1 else 2),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Top
            ) {
                items(state.items[page], key = { it.id }) {
                    StartCard(
                        start = it,
                        onItemClick = onClickItem
                    )
                }
            }
        }

        is StartsUiState.Failed -> {

            FailedScreen(
                message = (state).message,
                onClickRetry = {
                    onClickRetry()
                })

        }

        is StartsUiState.Loading -> {
            LoadingFullScreen()
        }
    }
}

