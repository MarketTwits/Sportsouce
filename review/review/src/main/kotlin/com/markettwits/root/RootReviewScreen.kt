package com.markettwits.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.news_event.NewsEventScreen
import com.markettwits.popular.presentation.PopularStartsScreen
import com.markettwits.random.root.presentation.RootRandomStartScreen
import com.markettwits.review.presentation.ReviewScreen
import com.markettwits.schedule.root.RootStartsScheduleScreen
import com.markettwits.start.root.RootStartScreen
import com.markettwits.start_filter.root.RootStartFilterScreen
import com.markettwits.start_search.root.RootStartsSearchScreen

@Composable
fun RootReviewScreen(component: RootReviewComponent) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootReviewComponent.Child.Review -> ReviewScreen(component = child.component)
            is RootReviewComponent.Child.Start -> RootStartScreen(child.component)
            is RootReviewComponent.Child.Filter -> RootStartFilterScreen(component = child.component)
            is RootReviewComponent.Child.Random -> RootRandomStartScreen(component = child.component)
            is RootReviewComponent.Child.Schedule -> RootStartsScheduleScreen(component = child.component)
            is RootReviewComponent.Child.Popular -> PopularStartsScreen(component = child.component)
            is RootReviewComponent.Child.NewsEvent -> NewsEventScreen(component = child.component)
            is RootReviewComponent.Child.Search -> RootStartsSearchScreen(component = child.component)
        }
    }
}