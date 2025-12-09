package com.markettwits.sportsouce.starts.favorites.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.markettwits.sportsouce.starts.favorites.presentation.store.StartsPopularStore.Message
import com.markettwits.sportsouce.starts.favorites.presentation.store.StartsPopularStore.State

internal object StartsPopularReducer : Reducer<State, Message> {
    override fun State.reduce(msg: Message): State {
        return when (msg) {
            is Message.Failed -> copy(
                isLoading = false,
                isError = true,
                starts = emptyList(),
                error = msg.error
            )

            is Message.Loaded -> copy(
                isLoading = false,
                isError = false,
                error = null,
                starts = msg.starts,
                removedStartIds = emptySet() // Reset removed IDs on refresh
            )

            is Message.Loading -> copy(isLoading = true)

            is Message.StartDeletionStarted -> copy(
                deletingStartIds = deletingStartIds + msg.startId
            )

            is Message.StartDeletionCompleted -> {
                if (msg.success) {
                    copy(
                        deletingStartIds = deletingStartIds - msg.startId,
                        removedStartIds = removedStartIds + msg.startId
                    )
                } else {
                    // If failed, just remove from deleting
                    copy(
                        deletingStartIds = deletingStartIds - msg.startId
                    )
                }
            }

            is Message.StartAdditionStarted -> copy(
                deletingStartIds = deletingStartIds + msg.startId
            )

            is Message.StartAdditionCompleted -> {
                if (msg.success) {
                    copy(
                        deletingStartIds = deletingStartIds - msg.startId,
                        removedStartIds = removedStartIds - msg.startId
                    )
                } else {
                    // If failed, just remove from deleting
                    copy(
                        deletingStartIds = deletingStartIds - msg.startId
                    )
                }
            }
        }
    }
}