package com.markettwits.sportsouce.bottom_bar.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.markettwits.core_ui.items.theme.SauceMotion
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.core_ui.items.components.navigation.LocalNavigationViewport
import com.markettwits.core_ui.items.components.navigation.LocalScrollContentBottomPadding
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
    aboveNavigation: @Composable () -> Unit = {},
    profilePhotoUrl: String? = null,
    systemBottomInsets: WindowInsets = WindowInsets.navigationBars.union(WindowInsets.displayCutout),
    content: @Composable () -> Unit,
) {
    val isLarge = calculateWindowSizeClass().isLarge
    val colors = SauceTheme.colors
    val haptics = LocalHapticFeedback.current
    val hazeState = rememberHazeState()
    val reservedBottomInsets = if (isLarge) systemBottomInsets.only(WindowInsetsSides.Bottom) else WindowInsets(0)
    val navigationViewport = remember(reservedBottomInsets) { NavigationViewport(reservedBottomInsets) }
    val density = LocalDensity.current
    val railWidth = 112.dp + 40.dp * (density.fontScale.coerceIn(1f, 2f) - 1f)
    val showRail = isLarge && isShowTopBar && items.isNotEmpty()
    var navigationHeight by remember { mutableIntStateOf(0) }
    var noticeHeight by remember { mutableIntStateOf(0) }
    val inheritedScrollPadding = LocalScrollContentBottomPadding.current
    val select: (BottomBarConfiguration) -> Unit = {
        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        onClickTab(it)
    }
    // The wide layout has no bottom dock to account for the Android taskbar.
    val safeBounds = if (isLarge) Modifier.background(colors.background).windowInsetsPadding(reservedBottomInsets) else Modifier
    Box(modifier.fillMaxSize().then(safeBounds)) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = colors.background,
            contentWindowInsets = WindowInsets(0),
            bottomBar = {
                Box(Modifier.onSizeChanged { navigationHeight = it.height }) {
                    if (!isLarge) AnimatedVisibility(isShowTopBar, enter = fadeIn(), exit = fadeOut()) {
                        GlassBottomNavigation(hazeState, items, selectedTab, isShowLabel, select, profilePhotoUrl)
                    }
                }
            },
        ) { padding ->
            val overlap = with(density) { padding.calculateBottomPadding().roundToPx() }
            Box(Modifier.fillMaxSize()
                .onGloballyPositioned { navigationViewport.update(it, overlap) }
                // Capture the composed screens and their transitions exactly once.
                .then(if (overlap > 0) Modifier.hazeSource(hazeState) else Modifier)
                .background(colors.background)) {
                CompositionLocalProvider(
                    LocalNavigationViewport provides navigationViewport,
                    LocalScrollContentBottomPadding provides inheritedScrollPadding + with(density) { noticeHeight.toDp() },
                ) {
                    Row(Modifier.fillMaxSize().consumeWindowInsets(padding)) {
                        if (showRail) WideScreenNavigation(
                            items = items,
                            selectedTab = selectedTab,
                            showLabels = isShowLabel,
                            profilePhotoUrl = profilePhotoUrl,
                            onSelect = select,
                            modifier = Modifier.width(railWidth).fillMaxHeight(),
                        )
                        Box(Modifier.weight(1f).fillMaxHeight()) { content() }
                    }
                }
            }
        }
        // Animate above a stable viewport. Resizing Scaffold's bottomBar on every reveal
        // frame exposes its background while descendant scroll/haze bounds catch up.
        Box(Modifier.align(Alignment.BottomCenter)
            .padding(start = if (showRail) railWidth else 0.dp,
                bottom = with(density) { navigationHeight.toDp() })) {
            Box(Modifier.onSizeChanged { noticeHeight = it.height }) { aboveNavigation() }
        }
    }
}

@Composable
internal fun NavigationIcon(
    item: BottomNavigationItem,
    selected: Boolean,
    hasLabel: Boolean,
    profilePhotoUrl: String?,
    size: Dp = 24.dp,
) {
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
        val photo = profilePhotoUrl?.takeIf { item.bottomBarConfiguration == BottomBarConfiguration.Profile && it.isNotBlank() }
        val description = if (hasLabel) null else item.title
        if (photo == null) {
            Icon(
                imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                contentDescription = description,
                modifier = Modifier.size(size),
            )
        } else {
            key(photo) {
                var loaded by remember { mutableStateOf(false) }
                val reveal by animateFloatAsState(
                    if (loaded) 1f else 0f,
                    tween(SauceMotion.feedbackMillis),
                    label = "Profile photo",
                )
                val foreground = LocalContentColor.current
                Box(Modifier.size(size).clip(CircleShape)) {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = description,
                        modifier = Modifier.matchParentSize().graphicsLayer { alpha = 1f - reveal },
                    )
                    AsyncImage(
                        model = photo,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize().graphicsLayer { alpha = reveal }
                            .border(1.dp, if (selected) foreground else colors.border, CircleShape),
                        onSuccess = { loaded = true },
                        onError = { loaded = false },
                    )
                }
            }
        }
    }
}

/** Kept independent of screen content so navigation states can be checked in isolation. */
@Composable
internal fun BottomNavigation(
    items: List<BottomNavigationItem>,
    selectedTab: BottomBarConfiguration,
    showLabels: Boolean,
    onSelect: (BottomBarConfiguration) -> Unit,
    modifier: Modifier = Modifier,
    profilePhotoUrl: String? = null,
) {
    if (items.isEmpty()) return

    val colors = SauceTheme.colors
    val shape = SauceShapes.button
    Row(modifier.padding(6.dp).fillMaxWidth().height(IntrinsicSize.Min).selectableGroup()) {
        items.forEach { item ->
            val selected = item.bottomBarConfiguration == selectedTab
            val foreground by animateColorAsState(
                if (selected) colors.action else colors.textSecondary,
                tween(SauceMotion.feedbackMillis),
                label = "Navigation foreground",
            )
            Column(
                modifier = Modifier.weight(1f).fillMaxHeight().heightIn(min = 52.dp)
                    .navigationTab(item, selected, shape, highlightInset = 2.dp) { onSelect(item.bottomBarConfiguration) }
                    .padding(horizontal = SauceSpacing.extraSmall, vertical = SauceSpacing.extraSmall),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
            ) {
                CompositionLocalProvider(LocalContentColor provides foreground) {
                    NavigationIcon(item, selected, showLabels, profilePhotoUrl)
                    if (showLabels) {
                        Text(
                            item.title,
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.labelMedium,
                            color = foreground,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}
