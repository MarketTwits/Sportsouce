package com.markettwits.review.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.review.data.ReviewRepository
import com.markettwits.review.domain.ActualStart
import com.markettwits.news_list.domain.NewsInfo
import com.markettwits.news_list.presentation.store.NewsStoreFactory
import com.markettwits.starts.StartsListItem
import kotlinx.coroutines.launch

class ReviewStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ReviewRepository
) {

    fun create(): ReviewStore =
        object : ReviewStore,
            Store<ReviewStore.Intent, ReviewStore.State, ReviewStore.Label> by storeFactory.create(
                name = "ReviewStore",
                initialState = ReviewStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data class InfoLoaded(val archive: List<StartsListItem>, val actual: List<StartsListItem>) :
            Msg

        data class InfoFailed(val message: String) : Msg
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<ReviewStore.Intent, Unit, ReviewStore.State, Msg, ReviewStore.Label>() {
        override fun executeIntent(intent: ReviewStore.Intent, getState: () -> ReviewStore.State) {
            when (intent) {
                is ReviewStore.Intent.Launch -> launch()
                is ReviewStore.Intent.OnClickItem -> publish(ReviewStore.Label.OnClickItem(intent.item))
                is ReviewStore.Intent.OnClickMenu -> publish(ReviewStore.Label.OnClickMenu(intent.item))
            }
        }

        override fun executeAction(action: Unit, getState: () -> ReviewStore.State) {
            launch()
        }

        private fun launch() {
            scope.launch {
                dispatch(Msg.Loading)
                repository.launch()
                    .onFailure {
                        dispatch(Msg.InfoFailed(it.message.toString()))
                    }
                    .onSuccess {
                        dispatch(Msg.InfoLoaded(archive = it[1], actual = it[0]))
                    }
            }
        }
    }

    private object ReducerImpl : Reducer<ReviewStore.State, Msg> {
        override fun ReviewStore.State.reduce(message: Msg): ReviewStore.State =
            when (message) {
                is Msg.InfoFailed -> ReviewStore.State(isError = true, message = message.message)
                is Msg.Loading -> ReviewStore.State(isLoading = true)
                is Msg.InfoLoaded -> ReviewStore.State(
                    archiveStarts = message.archive,
                    actualStarts = message.actual
                )
            }
    }
}