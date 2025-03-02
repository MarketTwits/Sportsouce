package com.markettwits.core.paging

import app.cash.paging.LoadStateError
import app.cash.paging.LoadStateLoading
import app.cash.paging.LoadStateNotLoading
import app.cash.paging.compose.LazyPagingItems

inline fun <T : Any> LazyPagingItems<T>.isPagingDataFailed(throwable: (Throwable) -> Unit) {
    if (this.loadState.refresh is LoadStateError)
        throwable((this.loadState.refresh as LoadStateError).error)
}

inline fun <T : Any> LazyPagingItems<T>.isPagingDataLoadingFirstTime(block: () -> Unit) {
    if (this.loadState.refresh is LoadStateLoading && itemCount == 0) block()
}

inline fun <T : Any> LazyPagingItems<T>.isPagingDataLoaded(block: () -> Unit) {
    if (this.loadState.append is LoadStateNotLoading && this.itemCount >= 1) block()
}

inline fun <T : Any> LazyPagingItems<T>.fold(
    onLoading: () -> Unit,
    onException: (Throwable) -> Unit,
    onSuccess: (isRefresh: Boolean) -> Unit,
    onEmpty: () -> Unit,
) {
    if (this.itemCount > 0) onSuccess(this.loadState.refresh is LoadStateLoading)
    if (this.loadState.refresh is LoadStateLoading && itemCount == 0) onLoading()
    if (this.loadState.refresh is LoadStateError)
        onException((this.loadState.refresh as LoadStateError).error)
    if (this.loadState.refresh is LoadStateNotLoading && itemCount == 0) onEmpty()
}




