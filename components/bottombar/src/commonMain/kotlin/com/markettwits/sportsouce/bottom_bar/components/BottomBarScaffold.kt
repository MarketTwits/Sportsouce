package com.markettwits.sportsouce.bottom_bar.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.sportsouce.bottom_bar.component.component.BottomBarComponent

@Composable
fun BottomBarScaffold(
    component: BottomBarComponent,
    modifier: Modifier = Modifier,
    aboveNavigation: @Composable () -> Unit = {},
    navigationVisible: Boolean = true,
    content: @Composable () -> Unit
) {

    val state by component.state.collectAsState()

    BottomBarContent(
        modifier = modifier,
        isShowTopBar = state.showTopBar && navigationVisible,
        isShowLabel = state.showLabel,
        items = state.items,
        selectedTab = state.selectedTab,
        content = content,
        aboveNavigation = aboveNavigation,
        onClickTab = { item ->
            component.onClickTabBar(item)
        },
    )
}