package com.markettwits.sportsouce.review.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.router.slot.child
import com.markettwits.news.news_event.screen.NewsEventScreen
import com.markettwits.selfupdater.components.notification.screen.NotificationScreen
import com.markettwits.selfupdater.components.selft_update.screen.SelfUpdateScreen
import com.markettwits.sportsouce.club.root.RootClubScreen
import com.markettwits.sportsouce.review.review.presentation.screen.ReviewScreen
import com.markettwits.sportsouce.settings.root.RootSettingsScreen
import com.markettwits.sportsouce.shop.root.RootShopCatalogScreen
import com.markettwits.sportsouce.start.root.RootStartScreen
import com.markettwits.sportsouce.start.search.root.RootStartsSearchScreen
import com.markettwits.sportsouce.starts.popular.root.RootStartsPopularScreen
import com.markettwits.sportsouce.starts.random.root.presentation.RootRandomStartScreen

@Composable
fun RootReviewScreen(component: RootReviewComponent) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootReviewComponent.Child.Review -> {
                ReviewScreen(
                    component = child.component,
                    notification = {
                        component.childSlot.child?.instance?.also { child ->
                            when (child) {
                                is RootReviewComponent.ChildSlot.Notification -> NotificationScreen(
                                    modifier = it,
                                    notificationComponent = child.component,
                                    renderer = child.render
                                )
                            }
                        }
                    })
            }

            is RootReviewComponent.Child.Start -> RootStartScreen(child.component)
            is RootReviewComponent.Child.Search -> RootStartsSearchScreen(component = child.component)
            is RootReviewComponent.Child.Random -> RootRandomStartScreen(component = child.component)
            //is RootReviewComponent.Child.Schedule -> RootStartsScheduleScreen(component = child.component)
            is RootReviewComponent.Child.Popular -> RootStartsPopularScreen(component = child.component)
            is RootReviewComponent.Child.NewsEvent -> NewsEventScreen(component = child.component)
            is RootReviewComponent.Child.Notification -> SelfUpdateScreen(component = child.component)
            is RootReviewComponent.Child.Settings -> RootSettingsScreen(component = child.component)
            is RootReviewComponent.Child.Club -> RootClubScreen(component = child.component)
            is RootReviewComponent.Child.Shop -> RootShopCatalogScreen(component = child.component)
        }
    }
}