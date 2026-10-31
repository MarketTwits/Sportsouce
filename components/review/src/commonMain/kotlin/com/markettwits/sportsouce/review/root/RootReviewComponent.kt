package com.markettwits.sportsouce.review.root

import com.markettwits.sportsouce.starts.albums.root.RootStartsAlbumsComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.selfupdater.components.notification.model.NewAppVersion
import com.markettwits.selfupdater.components.selft_update.component.SelfUpdateComponent
import com.markettwits.sportsauce.deeplink.model.Deeplink
import com.markettwits.sportsouce.club.root.RootClubComponent
import com.markettwits.sportsouce.news.common.model.NewsItem
import com.markettwits.sportsouce.news.news_event.component.NewsEventComponent
import com.markettwits.sportsouce.news.news_list.component.NewsComponent
import com.markettwits.sportsouce.news.root.RootNewsComponent
import com.markettwits.sportsouce.review.review.presentation.component.ReviewComponent
import com.markettwits.sportsouce.settings.root.RootSettingsComponent
import com.markettwits.sportsouce.shop.root.RootShopCatalogComponent
import com.markettwits.sportsouce.start.root.RootStartScreenComponent
import com.markettwits.sportsouce.start.search.root.RootStartsSearchComponent
import com.markettwits.sportsouce.starts.common.domain.StartsListItem
import com.markettwits.sportsouce.starts.popular.root.RootStartsPopularComponent
import com.markettwits.sportsouce.starts.random.root.presentation.RootStartRandomComponent
import kotlinx.serialization.Serializable
import com.markettwits.sportsouce.start.presentation.album.presentation.component.StartAlbumComponent

interface RootReviewComponent {
    val childStack: Value<ChildStack<*, Child>>
    fun openUpdate(version: NewAppVersion)

    fun handleDeeplink(deeplink: Deeplink.News)
    fun handleDeeplink(deeplink: Deeplink.Clubs)
    fun handleDeeplink(deeplink: Deeplink.Shop)

    @Serializable
    sealed interface Config {

        @Serializable
        data object Review : Config

        @Serializable
        data class Start(val startItem: StartsListItem) : Config

        @Serializable
        data object Random : Config

        @Serializable
        data object Popular : Config

        @Serializable
        data object Search : Config

        @Serializable
        data object Albums : Config

        @Serializable
        data class PhotoAlbum(val albumId: Int) : Config

        @Serializable
        data object News : Config

        @Serializable
        data class NewsEvent(val news: NewsItem) : Config

        @Serializable
        data class NewsEventById(val newsId: Int) : Config

        @Serializable
        data class Notification(val newAppVersion: NewAppVersion) : Config

        @Serializable
        data object Settings : Config

        @Serializable
        data object Club : Config

        @Serializable
        data class Shop(
            val categoryId: Int? = null,
            val productId: String? = null,
        ) : Config
    }

    sealed interface Child {
        data class Review(val component: ReviewComponent, val newsComponent: NewsComponent) : Child
        data class Start(val component: RootStartScreenComponent) : Child
        data class Random(val component: RootStartRandomComponent) : Child
        data class Popular(val component: RootStartsPopularComponent) : Child
        data class NewsEvent(val component: NewsEventComponent) : Child
        data class News(val component: RootNewsComponent) : Child
        data class Search(val component: RootStartsSearchComponent) : Child
        data class Albums(val component: RootStartsAlbumsComponent) : Child
        data class PhotoAlbum(val component: StartAlbumComponent) : Child
        data class Notification(val component: SelfUpdateComponent) : Child
        data class Settings(val component: RootSettingsComponent) : Child
        data class Club(val component: RootClubComponent) : Child
        data class Shop(val component: RootShopCatalogComponent) : Child
    }

}
