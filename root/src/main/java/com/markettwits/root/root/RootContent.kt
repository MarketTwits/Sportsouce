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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.markettwits.profile.presentation.DefaultProfileScreen
import com.markettwits.root.RootNewsScreen
import com.markettwits.root.RootReviewScreen
import com.markettwits.root.api.RootStartsScreen
import com.markettwits.root.bottom_bar.BottomBar

@Composable
fun RootContent(component: BaseRootComponent, modifier: Modifier = Modifier) {
    MaterialTheme {
        Surface(modifier = modifier, color = Color.White) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)),
            ) {
                Children(component = component, modifier = Modifier.weight(1F))
                BottomBar(component = component, modifier = Modifier)
            }
        }
    }
}
@Composable
private fun Children(component: BaseRootComponent, modifier: Modifier = Modifier) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        modifier = modifier,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.Starts -> RootStartsScreen(child.component)
            is RootComponent.Child.Profile ->  DefaultProfileScreen(child.component)
            is RootComponent.Child.Review -> RootReviewScreen(component = child.component)
        }
    }
}