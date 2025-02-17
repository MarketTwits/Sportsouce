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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.markettwits.bottom_bar.model.BottomNavigationItem
import com.markettwits.bottom_bar.model.BottomBarConfiguration
import com.markettwits.core_ui.items.theme.FontNunito

@Composable
internal fun BottomBarContent(
    modifier: Modifier = Modifier,
    isShowTopBar: Boolean,
    isShowLabel: Boolean,
    items: List<BottomNavigationItem>,
    selectedTab: BottomBarConfiguration,
    onClickTab: (BottomBarConfiguration) -> Unit,
) {
    AnimatedVisibility(
        visible = isShowTopBar,
        enter = slideInVertically() + expandVertically(expandFrom = Alignment.Top),
        exit = slideOutVertically() + shrinkVertically()
    ) {
        NavigationBar(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    selected = selectedTab == item.bottomBarConfiguration,
                    onClick = {
                        onClickTab(item.bottomBarConfiguration)
                    },
                    label = {
                        if (isShowLabel)
                            Text(
                                text = item.title,
                                color = MaterialTheme.colorScheme.tertiary,
                                fontFamily = FontNunito.medium()
                            )
                    },
                    alwaysShowLabel = isShowLabel,
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == item.bottomBarConfiguration) {
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