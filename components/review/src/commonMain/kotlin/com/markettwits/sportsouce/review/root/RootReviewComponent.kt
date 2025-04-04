package com.markettwits.sportsouce.review.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.markettwits.selfupdater.components.notification.component.InAppNotificationComponent
import com.markettwits.selfupdater.components.notification.model.NewAppVersion
import com.markettwits.selfupdater.components.selft_update.component.SelfUpdateComponent
import com.markettwits.sportsouce.club.root.RootClubComponent
import com.markettwits.sportsouce.inappnotification.api.InAppNotificationRenderer
import com.markettwits.sportsouce.news.common.model.NewsItem
import com.markettwits.sportsouce.news.news_event.component.NewsEventComponent
import com.markettwits.sportsouce.news.news_list.component.NewsComponent
import com.markettwits.sportsouce.review.review.presentation.component.ReviewComponent
import com.markettwits.sportsouce.settings.root.RootSettingsComponent
import com.markettwits.sportsouce.shop.root.RootShopCatalogComponent
import com.markettwits.sportsouce.start.root.RootStartScreenComponent
import com.markettwits.sportsouce.start.search.root.RootStartsSearchComponent
import com.markettwits.sportsouce.starts.popular.root.RootStartsPopularComponent
import com.markettwits.sportsouce.starts.random.root.presentation.RootStartRandomComponent
import kotlinx.serialization.Serializable

interface RootReviewComponent {
    val childStack: Value<ChildStack<*, Child>>
    val childSlot: Value<com.arkivanov.decompose.router.slot.ChildSlot<ConfigSlot, ChildSlot>>

    @Serializable
    sealed interface Config {

        @Serializable
        data object Review : Config

        @Serializable
        data class Start(val startId: Int) : Config

        @Serializable
        data object Random : Config

        @Serializable
        data object Popular : Config

        @Serializable
        data object Search : Config

        @Serializable
        data class NewsEvent(val news: NewsItem) : Config

        @Serializable
        data class Notification(val newAppVersion: NewAppVersion) : Config

        @Serializable
        data object Settings : Config

        @Serializable
        data object Club : Config

        @Serializable
        data object Shop : Config
    }

    sealed interface Child {
        data class Review(val component: ReviewComponent, val newsComponent: NewsComponent) : Child
        data class Start(val component: RootStartScreenComponent) : Child
        data class Random(val component: RootStartRandomComponent) : Child
        data class Popular(val component: RootStartsPopularComponent) : Child
        data class NewsEvent(val component: NewsEventComponent) : Child
        data class Search(val component: RootStartsSearchComponent) : Child
        data class Notification(val component: SelfUpdateComponent) : Child
        data class Settings(val component: RootSettingsComponent) : Child
        data class Club(val component: RootClubComponent) : Child
        data class Shop(val component: RootShopCatalogComponent) : Child
    }

    @Serializable
    sealed class ConfigSlot {
        @Serializable
        data object Notification : ConfigSlot()
    }

    sealed class ChildSlot {
        data class Notification(
            val component: InAppNotificationComponent,
            val render: InAppNotificationRenderer
        ) : ChildSlot()
    }
}