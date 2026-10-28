package com.markettwits.sportsouce.review.review.presentation.store


import com.arkivanov.mvikotlin.core.store.Store
import com.markettwits.core.errors.api.throwable.SauceError
import com.markettwits.sportsouce.news.common.model.NewsItem
import com.markettwits.sportsouce.review.review.domain.Review
import com.markettwits.sportsouce.review.review.domain.ReviewPhotoAlbum
import com.markettwits.sportsouce.review.review.presentation.store.ReviewStore.*
import com.markettwits.sportsouce.shop.domain.model.ShopItem
import com.markettwits.sportsouce.starts.common.domain.StartsListItem

interface ReviewStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class OnClickPhotoAlbum(val albumId: Int) : Intent
        data object OnRetryPhotoAlbums : Intent
        data object OnClickSearch : Intent
        data object OnClickSettings : Intent
        data class OnClickNews(val news: NewsItem) : Intent
        data class OnClickItem(val item: StartsListItem) : Intent
        data class OnClickMenu(val item : Int) : Intent
        data class OnClickProduct(val product: ShopItem) : Intent
        data object OnClickShowMoreSalesProducts : Intent
        data object OnClickShowMoreMerchProducts : Intent
        data object OnClickTelegram : Intent
        data object OnClickVk : Intent
        data class Launch(val forced: Boolean = false) : Intent
    }

    data class State(
        val isLoading : Boolean = false,
        val isRefreshing: Boolean = false,
        val hasLoaded: Boolean = false,
        val error: SauceError? = null,
        val review: Review = Review(),
        val photoAlbums: List<ReviewPhotoAlbum> = emptyList(),
        val photoAlbumsLoading: Boolean = false,
        val photoAlbumsFailed: Boolean = false,
    )

    sealed interface Label {
        data class OnClickPhotoAlbum(val albumId: Int) : Label
        data object OnClickSearch : Label
        data object OnClickSettings : Label
        data class OnClickNews(val news: NewsItem) : Label
        data class OnClickItem(val item: StartsListItem) : Label
        data class OnClickMenu(val item : Int) : Label
        data class OnClickProduct(val product: ShopItem) : Label
        data object OnClickShowMoreSalesProducts : Label
        data object OnClickShowMoreMerchProducts : Label
    }
}
