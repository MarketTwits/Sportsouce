package com.markettwits.sportsouce.review.review.presentation.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.markettwits.IntentAction
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.core.errors.api.throwable.mapToSauceError
import com.markettwits.core.log.LogTagProvider
import com.markettwits.core.log.errorLog
import com.markettwits.sportsouce.review.review.domain.Review
import com.markettwits.sportsouce.review.review.domain.ReviewRepository
import com.markettwits.sportsouce.review.review.domain.ReviewPhotoAlbum
import com.markettwits.sportsouce.review.review.domain.ReviewPhotoAlbumsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ReviewStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: ReviewRepository,
    private val intentAction: IntentAction,
    private val photoAlbumsRepository: ReviewPhotoAlbumsRepository,
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
        data object PhotoAlbumsLoading : Msg
        data class PhotoAlbumsLoaded(val items: List<ReviewPhotoAlbum>) : Msg
        data object PhotoAlbumsFailed : Msg
        data object Loading : Msg
        data class InfoLoaded(val review: Review) : Msg
        data class InfoFailed(val error: SauceError) : Msg
    }

    private inner class ExecutorImpl(
        private val intentAction: IntentAction,
    ) : CoroutineExecutor<ReviewStore.Intent, Unit, ReviewStore.State, Msg, ReviewStore.Label>(), LogTagProvider {

        override val tag: String = "ReviewStoreFactory"
        private var launchJob: Job? = null
        private var photoAlbumsJob: Job? = null

        override fun executeIntent(intent: ReviewStore.Intent) {
            when (intent) {
                is ReviewStore.Intent.Launch -> { launch(intent.forced); loadPhotoAlbums() }
                is ReviewStore.Intent.OnClickPhotoAlbum -> if (state().photoAlbums.any { it.id == intent.albumId }) {
                    publish(ReviewStore.Label.OnClickPhotoAlbum(intent.albumId))
                }
                ReviewStore.Intent.OnRetryPhotoAlbums -> if (photoAlbumsJob?.isActive != true) loadPhotoAlbums()
                is ReviewStore.Intent.OnClickItem -> publish(ReviewStore.Label.OnClickItem(intent.item))
                is ReviewStore.Intent.OnClickMenu -> publish(ReviewStore.Label.OnClickMenu(intent.item))
                is ReviewStore.Intent.OnClickNews -> publish(ReviewStore.Label.OnClickNews(intent.news))
                is ReviewStore.Intent.OnClickProduct -> publish(ReviewStore.Label.OnClickProduct(intent.product))
                is ReviewStore.Intent.OnClickShowMoreSalesProducts -> publish(ReviewStore.Label.OnClickShowMoreSalesProducts)
                is ReviewStore.Intent.OnClickShowMoreMerchProducts -> publish(ReviewStore.Label.OnClickShowMoreMerchProducts)
                is ReviewStore.Intent.OnClickSearch -> publish(ReviewStore.Label.OnClickSearch)
                is ReviewStore.Intent.OnClickSettings -> publish(ReviewStore.Label.OnClickSettings)
                is ReviewStore.Intent.OnClickTelegram -> intentAction.openWebPage(SPORT_SAUCE_TG_URL)
                is ReviewStore.Intent.OnClickVk -> intentAction.openWebPage(SPORT_SAUCE_VK_URL)
            }
        }

        override fun executeAction(action: Unit) {
            launch(false)
            loadPhotoAlbums()
        }

        private fun loadPhotoAlbums() {
            photoAlbumsJob?.cancel()
            photoAlbumsJob = scope.launch {
                photoAlbumsRepository.albums()
                    .onStart { dispatch(Msg.PhotoAlbumsLoading) }
                    .catch { dispatch(Msg.PhotoAlbumsFailed) }
                    .collect { dispatch(Msg.PhotoAlbumsLoaded(it)) }
            }
        }

        private fun launch(forced: Boolean) {
            launchJob?.cancel()
            launchJob = scope.launch {
                repository.review(forced)
                    .onStart {
                        dispatch(Msg.Loading)
                    }
                    .catch {
                        errorLog(it) { "Failed when launch review content" }
                        dispatch(Msg.InfoFailed(it.mapToSauceError()))
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
                Msg.PhotoAlbumsLoading -> copy(photoAlbumsLoading = true, photoAlbumsFailed = false)
                is Msg.PhotoAlbumsLoaded -> copy(photoAlbums = msg.items, photoAlbumsLoading = false, photoAlbumsFailed = false)
                Msg.PhotoAlbumsFailed -> copy(photoAlbumsLoading = false, photoAlbumsFailed = true)
                is Msg.InfoFailed -> copy(
                    isLoading = false,
                    error = msg.error,
                )

                is Msg.Loading -> copy(isLoading = true, error = null)

                is Msg.InfoLoaded -> copy(
                    isLoading = false,
                    error = null,
                    review = msg.review
                )
            }
    }
}
