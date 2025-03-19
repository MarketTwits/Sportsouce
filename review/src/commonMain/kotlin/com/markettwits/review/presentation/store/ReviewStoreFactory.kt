package com.markettwits.review.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.IntentAction
import com.markettwits.core.errors.api.throwable.networkExceptionHandler
import com.markettwits.core.log.LogTagProvider
import com.markettwits.core.log.errorLog
import com.markettwits.review.domain.Review
import com.markettwits.review.domain.ReviewRepository
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
    ) : CoroutineExecutor<ReviewStore.Intent, Unit, ReviewStore.State, Msg, ReviewStore.Label>(), LogTagProvider {

        override val tag: String = "ReviewStoreFactory"

        override fun executeIntent(intent: ReviewStore.Intent) {
            when (intent) {
                is ReviewStore.Intent.Launch -> launch(intent.forced)
                is ReviewStore.Intent.OnClickItem -> publish(ReviewStore.Label.OnClickItem(intent.item))
                is ReviewStore.Intent.OnClickMenu -> publish(ReviewStore.Label.OnClickMenu(intent.item))
                is ReviewStore.Intent.OnClickNews -> publish(ReviewStore.Label.OnClickNews(intent.news))
                is ReviewStore.Intent.OnClickSearch -> publish(ReviewStore.Label.OnClickSearch)
                is ReviewStore.Intent.OnClickSettings -> publish(ReviewStore.Label.OnClickSettings)
                is ReviewStore.Intent.OnClickTelegram -> intentAction.openWebPage(SPORT_SAUCE_TG_URL)
                is ReviewStore.Intent.OnClickVk -> intentAction.openWebPage(SPORT_SAUCE_VK_URL)
            }
        }

        override fun executeAction(action: Unit) {
            launch(false)
        }

        private fun launch(forced: Boolean) {
            scope.launch {
                repository.review(forced)
                    .onStart {
                        dispatch(Msg.Loading)
                    }
                    .catch {
                        errorLog(it) { "Failed when launch review content" }
                        dispatch(Msg.InfoFailed(it.networkExceptionHandler().message.toString()))
                    }
                    .collect { result ->
                        dispatch(Msg.InfoLoaded(result))
                    }
            }
        }
    }

    private companion object {
        const val SPORT_SAUCE_TG_URL = "https://t.me/sportsauce"
        const val SPORT_SAUCE_VK_URL = "https://vk.com/sportsoyuznsk"
    }

    private object ReducerImpl : Reducer<ReviewStore.State, Msg> {
        override fun ReviewStore.State.reduce(msg: Msg): ReviewStore.State =
            when (msg) {
                is Msg.InfoFailed -> copy(
                    isError = true,
                    isLoading = false,
                    message = msg.message
                )

                is Msg.Loading -> copy(isLoading = true, isError = false)

                is Msg.InfoLoaded -> copy(
                    isLoading = false,
                    isError = false,
                    review = msg.review
                )
            }
    }
}