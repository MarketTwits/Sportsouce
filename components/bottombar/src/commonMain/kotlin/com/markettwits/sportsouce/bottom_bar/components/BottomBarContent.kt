package com.markettwits.sportsouce.bottom_bar.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.components.navigation.LocalNavigationViewport
import com.markettwits.core_ui.items.components.navigation.NavigationViewport
import com.markettwits.core_ui.items.window.calculateWindowSizeClass
import com.markettwits.core_ui.items.window.isLarge
import com.markettwits.sportsouce.bottom_bar.model.BottomBarConfiguration
import com.markettwits.sportsouce.bottom_bar.model.BottomNavigationItem
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun BottomBarContent(
    modifier: Modifier = Modifier,
    isShowTopBar: Boolean,
    isShowLabel: Boolean,
    items: List<BottomNavigationItem>,
    selectedTab: BottomBarConfiguration,
    onClickTab: (BottomBarConfiguration) -> Unit,
    content: @Composable () -> Unit,
) {
    val isLarge = calculateWindowSizeClass().isLarge
    val colors = SauceTheme.colors
    val haptics = LocalHapticFeedback.current
    val hazeState = rememberHazeState()
    val navigationViewport = remember(hazeState) {
        NavigationViewport { key -> Modifier.hazeSource(hazeState, zIndex = 1f, key = key) }
    }
    val density = LocalDensity.current
    val select: (BottomBarConfiguration) -> Unit = {
        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
        onClickTab(it)
    }
    Scaffold(
        modifier = modifier,
        containerColor = colors.background,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (!isLarge) AnimatedVisibility(isShowTopBar, enter = fadeIn(), exit = fadeOut()) {
                GlassBottomNavigation(hazeState, items, selectedTab, isShowLabel, select)
            }
        },
    ) { padding ->
        // Capture only the content, never the navigation icons or the glass itself.
        val overlap = if (!isLarge && isShowTopBar) with(density) { padding.calculateBottomPadding().roundToPx() } else 0
        Box(Modifier.fillMaxSize()
            .onGloballyPositioned { navigationViewport.update(it, overlap) }
            .then(if (!isLarge && isShowTopBar) Modifier.hazeSource(hazeState) else Modifier)) {
            CompositionLocalProvider(
                LocalNavigationViewport provides navigationViewport
            ) {
                Row(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding)) {
                    if (isLarge && isShowTopBar) NavigationRail(
                        containerColor = colors.surface,
                        contentColor = colors.textPrimary,
                    ) {
                        items.forEach { item ->
                            val selected = selectedTab == item.bottomBarConfiguration
                            NavigationRailItem(
                                modifier = Modifier.navigationNotifications(item),
                                selected = selected,
                                onClick = { select(item.bottomBarConfiguration) },
                                icon = { NavigationIcon(item, selected, isShowLabel) },
                                label = if (isShowLabel) { { NavigationLabel(item.title, selected) } } else null,
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = colors.action,
                                    selectedTextColor = colors.action,
                                    indicatorColor = colors.actionSoft,
                                    unselectedIconColor = colors.textMuted,
                                    unselectedTextColor = colors.textMuted,
                                ),
                            )
                        }
                    }
                    Box(Modifier.weight(1f).fillMaxHeight()) { content() }
                }
            }
        }
    }
}

@Composable
private fun NavigationIcon(item: BottomNavigationItem, selected: Boolean, hasLabel: Boolean) {
    val count = item.badgeCount?.takeIf { it > 0 }
    val colors = SauceTheme.colors
    BadgedBox(
        badge = {
            if (count != null) Badge(
                modifier = Modifier.clearAndSetSemantics {},
                containerColor = colors.material.error,
                contentColor = colors.material.onError,
            ) {
                Text(if (count > 99) "99+" else count.toString())
            }
            else if (item.hasNews) Badge(
                modifier = Modifier.clearAndSetSemantics {},
                containerColor = colors.material.error,
            )
        },
    ) {
        Icon(
            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
            contentDescription = if (hasLabel) null else item.title,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
private fun NavigationLabel(title: String, selected: Boolean) {
    Text(title, modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.labelMedium,
        textAlign = TextAlign.Center,
        fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal)
}

/** Kept independent of screen content so navigation states can be checked in isolation. */
@Composable
internal fun BottomNavigation(
    items: List<BottomNavigationItem>,
    selectedTab: BottomBarConfiguration,
    showLabels: Boolean,
    onSelect: (BottomBarConfiguration) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = SauceTheme.colors.surface,
    glass: Boolean = false,
) {
    val colors = SauceTheme.colors
    NavigationBar(modifier = modifier, containerColor = containerColor, contentColor = colors.textPrimary, tonalElevation = 0.dp) {
        items.forEach { item ->
            val selected = selectedTab == item.bottomBarConfiguration
            NavigationBarItem(
                modifier = Modifier.navigationNotifications(item),
                selected = selected,
                onClick = { onSelect(item.bottomBarConfiguration) },
                icon = { NavigationIcon(item, selected, showLabels) },
                label = if (showLabels) { { NavigationLabel(item.title, selected) } } else null,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colors.action,
                    selectedTextColor = colors.action,
                    indicatorColor = colors.actionSoft,
                    unselectedIconColor = if (glass) colors.textSecondary else colors.textMuted,
                    unselectedTextColor = if (glass) colors.textSecondary else colors.textMuted,
                ),
            )
        }
    }
}

private fun Modifier.navigationNotifications(item: BottomNavigationItem): Modifier = semantics {
    val count = item.badgeCount?.takeIf { it > 0 }
    if (count != null) stateDescription = "Уведомлений: $count"
    else if (item.hasNews) stateDescription = "Есть обновления"
}
