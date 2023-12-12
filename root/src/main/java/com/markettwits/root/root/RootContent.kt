package com.markettwits.root.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.profile.ProfileScreen
import com.markettwits.start.StartScreen
import com.markettwits.starts.DefaultStartsScreen
import com.markettwits.starts.StartsScreen
import com.markettwits.root.root.bottom_bar.BottomBar

@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    MaterialTheme {
        Surface(modifier = modifier, color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)),
            ) {
                Children(component = component, modifier = Modifier.weight(1F))
                BottomBar(component = component, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
@Composable
private fun Children(component: RootComponent, modifier: Modifier = Modifier) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        modifier = modifier,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.Starts -> DefaultStartsScreen(child.component)
            is RootComponent.Child.Profile ->  ProfileScreen()
        }
    }
}