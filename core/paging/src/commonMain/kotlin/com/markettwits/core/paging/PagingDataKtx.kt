package com.markettwits.core.paging

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

fun <T : Any> loadingPagingDataFlow(): Flow<PagingData<T>> =
     flowOf(
        PagingData.from(
            emptyList(),
            LoadStates(
                append = LoadState.NotLoading(endOfPaginationReached = false),
                refresh = LoadState.Loading,
                prepend = LoadState.NotLoading(endOfPaginationReached = false)
            )
        )
    )

inline fun <T : Any> LazyPagingItems<T>.isPagingDataFailed(throwable: (Throwable) -> Unit) {
    if (this.loadState.refresh is LoadState.Error)
        throwable((this.loadState.refresh as LoadState.Error).error)
}

inline fun <T : Any> LazyPagingItems<T>.isPagingDataLoadingFirstTime(block: () -> Unit) {
    if (this.loadState.refresh is LoadState.Loading && itemCount == 0) block()
}

inline fun <T : Any> LazyPagingItems<T>.isPagingDataLoaded(block: () -> Unit) {
    if (this.loadState.append is LoadState.NotLoading && this.itemCount >= 1) block()
}

inline fun <T : Any> LazyPagingItems<T>.fold(
    onLoading: () -> Unit,
    onException: (Throwable) -> Unit,
    onSuccess: (isRefresh: Boolean) -> Unit,
    onEmpty: () -> Unit,
) {
    if (this.itemCount > 0) onSuccess(this.loadState.refresh is LoadState.Loading)
    if (this.loadState.refresh is LoadState.Loading && itemCount == 0) onLoading()
    if (this.loadState.refresh is LoadState.Error)
        onException((this.loadState.refresh as LoadState.Error).error)
    if (this.loadState.refresh is LoadState.NotLoading && itemCount == 0) onEmpty()
}




