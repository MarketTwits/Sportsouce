package com.markettwits.starts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.markettwits.core_ui.theme.SportSouceTheme
import com.markettwits.start.presentation.common.LoadingScreen
import com.markettwits.start.presentation.membres.screen.StartMembersScreen
import com.markettwits.start.presentation.start.screen.StartScreen
import com.markettwits.starts.components.success.StartCard
import com.markettwits.topbar.TabBar


@Preview
@Composable
private fun StartsScreenPreview() {
    SportSouceTheme {
        StartsScreen(MockStartsScreenComponent())
    }
}

@Composable
fun StartsScreen(component: StartsScreen) {
    val starts by component.starts.subscribeAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    {
        when (starts) {
            is StartsUiState.Success -> {
                Column {
                    TabBar(content = { page ->
                        LazyColumn {
                            items((starts as StartsUiState.Success).items[page]) {
                                StartCard(start = it, onItemClick = {
                                    component.onItemClick(it)
                                })
                            }
                        }
                    })
                }
            }

            is StartsUiState.Failed -> {
                Text(text = "error : ${(starts as StartsUiState.Failed).message}", color = Color.Red)
            }

            is StartsUiState.Loading -> {
                LoadingScreen()
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
            is DefaultStartsComponent.Child.StartMembers -> StartMembersScreen(component = child.component)
        }
    }
}
