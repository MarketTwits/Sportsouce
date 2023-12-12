package com.markettwits.starts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.start.StartScreen
import com.markettwits.starts.components.loading.StartScreenLoading
import com.markettwits.starts.components.success.StartCard


@Preview
@Composable
private fun StartsScreenPreview() {
    StartsScreen(MockStartsScreenComponent())
}

@Composable
fun StartsScreen(component: StartsScreen) {
    val starts by component.starts.subscribeAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        when (starts) {
            is StartsUiState.Success -> {
                LazyColumn {
                    items((starts as StartsUiState.Success).items) {
                        StartCard(start = it, onItemClick = {
                            component.onItemClick(it)
                        })
                    }
                }
            }

            is StartsUiState.Loading -> {
                StartScreenLoading()
            }

            else -> {}
        }
    }
}

@Composable
fun DefaultStartsScreen(component: DefaultStartsComponent) {
    com.arkivanov.decompose.extensions.compose.stack.Children(
        stack = component.childStack,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is DefaultStartsComponent.Child.Start -> StartScreen(component = child.component)
            is DefaultStartsComponent.Child.Starts -> StartsScreen(component = child.component)
        }
    }
}
