package com.markettwits.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.news_event.NewsEventScreen
import com.markettwits.news_list.presentation.NewsScreen
import com.markettwits.review.presentation.ReviewScreen
import com.markettwits.start.presentation.start.screen.StartScreen
import com.markettwits.start.root.RootStartScreen

@Composable
fun RootReviewScreen(component: RootReviewComponent) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootReviewComponent.Child.Review -> ReviewScreen(component = child.component, newsComponent = child.newsComponent)
            is RootReviewComponent.Child.Start -> RootStartScreen(child.component)
        }
    }
}