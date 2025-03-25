package com.markettwits.sportsouce.bottom_bar.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.window.calculateWindowSizeClass
import com.markettwits.core_ui.items.window.isLarge
import com.markettwits.sportsouce.bottom_bar.model.BottomBarConfiguration
import com.markettwits.sportsouce.bottom_bar.model.BottomNavigationItem

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun BottomBarContent(
    modifier: Modifier = Modifier,
    isShowTopBar: Boolean,
    isShowLabel: Boolean,
    items: List<BottomNavigationItem>,
    selectedTab: BottomBarConfiguration,
    onClickTab: (BottomBarConfiguration) -> Unit,
    content: @Composable () -> Unit
) {

    val state = rememberNavigationSuiteScaffoldState()

    val isLarge = calculateWindowSizeClass().isLarge

    val itemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
    )
    val textColor = MaterialTheme.colorScheme.tertiary

    val itemModifier = if (isLarge) Modifier.padding(6.dp) else Modifier.padding(2.dp)

    LaunchedEffect(isShowTopBar) {
        if (isShowTopBar) {
            state.show()
        } else {
            if (!isLarge)
                state.hide()
        }
    }

    NavigationSuiteScaffold(
        state = state,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = MaterialTheme.colorScheme.primary,
            navigationRailContainerColor = MaterialTheme.colorScheme.primary,
        ),
        navigationSuiteItems = {
            items.forEach { item ->
                val isSelected = selectedTab == item.bottomBarConfiguration
                val color = if (isSelected) textColor else Color.Gray
                item(
                    modifier = itemModifier,
                    colors = itemColors,
                    selected = isSelected,
                    onClick = {
                        onClickTab(item.bottomBarConfiguration)
                    },
                    label = {
                        if (isShowLabel)
                            Text(
                                text = item.title,
                                color = color,
                                fontFamily = if (isSelected) FontNunito.bold() else FontNunito.medium()
                            )
                    },
                    alwaysShowLabel = isShowLabel,
                    icon = {
                        Icon(
                            imageVector = if (isSelected) {
                                item.selectedIcon
                            } else
                                item.unselectedIcon,
                            contentDescription = item.title,
                            tint = color
                        )
                    }
                )
            }
        }
    ) {
        content()
    }


}