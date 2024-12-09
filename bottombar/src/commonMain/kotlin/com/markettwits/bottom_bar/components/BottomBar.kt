package com.markettwits.bottom_bar.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.markettwits.bottom_bar.component.component.BottomBarComponent
import com.markettwits.core_ui.items.theme.LocalDarkOrLightTheme

@Composable
fun BottomBar(component: BottomBarComponent, modifier: Modifier = Modifier) {

    val state by component.state.collectAsState()

    BottomBarContent(
        modifier = modifier,
        isShowTopBar = state.showTopBar,
        isShowLabel = state.showLabel,
        items = state.items,
        selectedTab = state.selectedTab,
        onClickTab = { item ->
            component.onClickTabBar(item)
        }
    )
}