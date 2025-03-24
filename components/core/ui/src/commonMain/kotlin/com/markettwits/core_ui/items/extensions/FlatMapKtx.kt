package com.markettwits.core_ui.items.extensions

inline fun <R, T> Result<R>.flatMap(transform: (R) -> Result<T>) {
    fold(
        onSuccess = { transform(it) },
        onFailure = { Result.failure(it) }
    )
}

inline fun <R, T> Result<R>.flatMapCallback(transform: (R) -> Result<T>): Result<T> {
    fold(
        onSuccess = { return transform(it) },
        onFailure = { return Result.failure(it) }
    )
}
