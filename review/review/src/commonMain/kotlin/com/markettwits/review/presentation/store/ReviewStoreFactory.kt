package com.markettwits.review.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.IntentAction
import com.markettwits.cloud.exception.networkExceptionHandler
import com.markettwits.review.data.ReviewRepository
import com.markettwits.review.domain.Review
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ReviewStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ReviewRepository,
    private val intentAction: IntentAction
) {

    fun create(): ReviewStore =
        object : ReviewStore,
            Store<ReviewStore.Intent, ReviewStore.State, ReviewStore.Label> by storeFactory.create(
                name = "ReviewStore",
                initialState = ReviewStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = { ExecutorImpl(intentAction) },
                reducer = ReducerImpl
            ) {}

    private sealed interface Msg {
        data object Loading : Msg
        data class InfoLoaded(val review: Review) : Msg
        data class InfoFailed(val message: String) : Msg
    }

    private inner class ExecutorImpl(
        private val intentAction: IntentAction
    ) : CoroutineExecutor<ReviewStore.Intent, Unit, ReviewStore.State, Msg, ReviewStore.Label>() {
        override fun executeIntent(intent: ReviewStore.Intent, getState: () -> ReviewStore.State) {
            when (intent) {
                is ReviewStore.Intent.Launch -> launch(intent.forced)
                is ReviewStore.Intent.OnClickItem -> publish(ReviewStore.Label.OnClickItem(intent.item))
                is ReviewStore.Intent.OnClickMenu -> publish(ReviewStore.Label.OnClickMenu(intent.item))
                is ReviewStore.Intent.OnClickNews -> publish(ReviewStore.Label.OnClickNews(intent.news))
                is ReviewStore.Intent.OnClickSearch -> publish(ReviewStore.Label.OnClickSearch)
                is ReviewStore.Intent.OnClickTelegram -> intentAction.openWebPage(SPORT_SAUCE_TG_URL)
                is ReviewStore.Intent.OnClickVk -> intentAction.openWebPage(SPORT_SAUCE_VK_URL)

            }
        }

        override fun executeAction(action: Unit, getState: () -> ReviewStore.State) {
            launch(false)
        }

        private fun launch(forced: Boolean) {
            scope.launch {
                repository.review(forced)
                    .onStart {
                        dispatch(Msg.Loading)
                    }
                    .catch {
                        dispatch(Msg.InfoFailed(networkExceptionHandler(it).message.toString()))
                    }
                    .collect { result ->
                        dispatch(Msg.InfoLoaded(result))
                    }
            }
        }
    }

    private companion object {
        const val SPORT_SAUCE_TG_URL = "hhttps://t.me/sportsauce"
        const val SPORT_SAUCE_VK_URL = "https://vk.com/sportsoyuznsk"
    }

    private object ReducerImpl : Reducer<ReviewStore.State, Msg> {
        override fun ReviewStore.State.reduce(message: Msg): ReviewStore.State =
            when (message) {
                is Msg.InfoFailed -> copy(
                    isError = true,
                    isLoading = false,
                    message = message.message
                )

                is Msg.Loading -> copy(isLoading = true, isError = false)
                is Msg.InfoLoaded -> copy(
                    isLoading = false,
                    isError = false,
                    review = message.review
                )
            }
    }
}