package com.markettwits.bottom_bar.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.markettwits.bottom_bar.component.BottomBarComponent

@Composable
fun BottomBar(component: BottomBarComponent, modifier: Modifier = Modifier) {

    val state by component.state.collectAsState()

    AnimatedVisibility(
        visible = state.showTopBar,
        enter = slideInVertically() + expandVertically(expandFrom = Alignment.Top),
        exit = slideOutVertically() + shrinkVertically()
    ) {
        NavigationBar(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            state.items.forEach { item ->
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    selected = state.selectedTab == item.configuration,
                    onClick = {
                        component.onClickTabBar(item.configuration)
                    },
                    label = {
                        Text(text = item.title, color = MaterialTheme.colorScheme.tertiary)
                    },
                    alwaysShowLabel = true,
                    icon = {
                        Icon(
                            imageVector = if (state.selectedTab == item.configuration) {
                                item.selectedIcon
                            } else
                                item.unselectedIcon,
                            contentDescription = item.title,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                )
            }
        }
    }
}