package com.markettwits.starts.starts.presentation.components

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.markettwits.core_ui.base_screen.FailedScreen
import com.markettwits.core_ui.base_screen.LoadingFullScreen
import com.markettwits.starts.starts.presentation.component.StartsUiState
import com.markettwits.starts_common.presentation.StartCard
import kotlinx.coroutines.launch
import okhttp3.internal.notify

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun StartsScreenList(
    modifier: Modifier = Modifier,
    notification: @Composable() ((Modifier) -> Unit),
    showNotification: Boolean,
    state: StartsUiState,
    page: Int,
    onClickItem: (Int) -> Unit,
    onClickRetry: () -> Unit
) {

    val listState = rememberLazyListState()

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        notification(Modifier)
        when (state) {
            is StartsUiState.Success -> {
                val items = state.items[page]
                items.forEach {
                    key(it.id) {
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

//    LazyColumn(
//        modifier = modifier,
//        state = listState
//    ) {
//        if (showNotification){
//            item { notification(Modifier) }
//        }
//        when (state) {
//            is StartsUiState.Success -> {
//                val items = state.items[page]
//                items(items, key = { it.id }) {
//                    StartCard(
//                        start = it,
//                        onItemClick = onClickItem
//                    )
//                }
//            }
//
//            is StartsUiState.Failed -> {
//
//                item {
//                    FailedScreen(
//                        message = (state).message,
//                        onClickRetry = {
//                            onClickRetry()
//                        })
//                }
//
//            }
//
//            is StartsUiState.Loading -> {
//                item {
//                    LoadingFullScreen()
//                }
//            }
//        }
//    }
//    LaunchedEffect(state) {
//        listState.animateScrollToItem(0,-400)
//    }
}