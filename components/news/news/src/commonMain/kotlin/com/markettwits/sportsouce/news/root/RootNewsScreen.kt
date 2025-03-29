package com.markettwits.sportsouce.news.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.sportsouce.news.news_event.screen.NewsEventScreen
import com.markettwits.sportsouce.news.news_list.screen.NewsScreen

@Composable
fun RootNewsScreen(component: RootNewsComponent) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootNewsComponent.Child.News -> NewsScreen(component = child.component)
            is RootNewsComponent.Child.NewsEvent -> NewsEventScreen(child.component)
        }
    }
}