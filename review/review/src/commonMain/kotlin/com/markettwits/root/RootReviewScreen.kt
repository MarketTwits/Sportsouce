package com.markettwits.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.router.slot.child
import com.markettwits.club.root.RootClubScreen
import com.markettwits.news_event.screen.NewsEventScreen
import com.markettwits.popular.root.RootStartsPopularScreen
import com.markettwits.random.root.presentation.RootRandomStartScreen
import com.markettwits.review.presentation.screen.ReviewScreen
import com.markettwits.schedule.root.RootStartsScheduleScreen
import com.markettwits.selfupdater.components.notification.screen.NotificationScreen
import com.markettwits.selfupdater.components.selft_update.screen.SelfUpdateScreen
import com.markettwits.settings.root.RootSettingsScreen
import com.markettwits.shop.root.RootShopCatalogScreen
import com.markettwits.start.root.RootStartScreen
import com.markettwits.start_search.root.RootStartsSearchScreen

@Composable
fun RootReviewScreen(component: RootReviewComponent) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootReviewComponent.Child.Review -> ReviewScreen(
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
                }
            )
            is RootReviewComponent.Child.Start -> RootStartScreen(child.component)
            is RootReviewComponent.Child.Search -> RootStartsSearchScreen(component = child.component)
            is RootReviewComponent.Child.Random -> RootRandomStartScreen(component = child.component)
            is RootReviewComponent.Child.Schedule -> RootStartsScheduleScreen(component = child.component)
            is RootReviewComponent.Child.Popular -> RootStartsPopularScreen(component = child.component)
            is RootReviewComponent.Child.NewsEvent -> NewsEventScreen(component = child.component)
            is RootReviewComponent.Child.Notification -> SelfUpdateScreen(component = child.component)
            is RootReviewComponent.Child.Settings -> RootSettingsScreen(component = child.component)
            is RootReviewComponent.Child.Club -> RootClubScreen(component = child.component)
            is RootReviewComponent.Child.Shop -> RootShopCatalogScreen(component = child.component)
        }
    }
}