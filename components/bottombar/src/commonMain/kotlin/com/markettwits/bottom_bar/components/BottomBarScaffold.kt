package com.markettwits.bottom_bar.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.bottom_bar.component.component.BottomBarComponent

@Composable
fun BottomBarScaffold(
    component: BottomBarComponent,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    val state by component.state.collectAsState()

    BottomBarContent(
        modifier = modifier,
        isShowTopBar = state.showTopBar,
        isShowLabel = state.showLabel,
        items = state.items,
        selectedTab = state.selectedTab,
        onClickTab = { item ->
            component.onClickTabBar(item)
        },
        content = content
    )
}