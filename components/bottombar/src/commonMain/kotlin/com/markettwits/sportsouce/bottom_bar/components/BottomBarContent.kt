package com.markettwits.sportsouce.bottom_bar.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    content: @Composable () -> Unit,
) {
    val isLarge = calculateWindowSizeClass().isLarge

    if (isLarge) {
        // Use Scaffold with custom navigation rail for large screens
        Scaffold(
            modifier = modifier,
            contentWindowInsets = WindowInsets(0),
            containerColor = MaterialTheme.colorScheme.primary,
        ) { paddingValues ->
            Row {
                AnimatedVisibility(
                    visible = isShowTopBar,
                    enter = slideInVertically(
                        initialOffsetY = { -it }
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { -it }
                    )
                ) {
                    CustomNavigationRail(
                        items = items,
                        selectedTab = selectedTab,
                        isShowLabel = isShowLabel,
                        onClickTab = onClickTab
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    content()
                }
            }
        }
    } else {
        // Use Scaffold with custom bottom navigation for mobile screens
        Scaffold(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.primary,
            bottomBar = {
                AnimatedVisibility(
                    visible = isShowTopBar,
                    enter = fadeIn(
                        animationSpec = tween(durationMillis = 300)
                    ),
                    exit = fadeOut(
                        animationSpec = tween(durationMillis = 300)
                    )
                ) {
                    CustomBottomNavigation(
                        items = items,
                        selectedTab = selectedTab,
                        isShowLabel = isShowLabel,
                        onClickTab = onClickTab
                    )
                }
            },
            contentWindowInsets = WindowInsets(0),
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun CustomBottomNavigation(
    items: List<BottomNavigationItem>,
    selectedTab: BottomBarConfiguration,
    isShowLabel: Boolean,
    onClickTab: (BottomBarConfiguration) -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    Surface(
        color = MaterialTheme.colorScheme.primary,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(NavigationBarDefaults.windowInsets)
                .defaultMinSize(minHeight = 76.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEach { item ->
                val isSelected = selectedTab == item.bottomBarConfiguration
                val selectedColor = MaterialTheme.colorScheme.tertiary
                val unselectedColor = MaterialTheme.colorScheme.outline
                val color = if (isSelected) selectedColor else unselectedColor
                val interactionSource = remember { MutableInteractionSource() }

                Column(
                    modifier = Modifier
                        .weight(1F)
                        .selectable(
                            selected = isSelected,
                            onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                onClickTab(item.bottomBarConfiguration)
                            },
                            role = Role.Tab,
                            interactionSource = interactionSource,
                            indication = null,
                        )
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box {
                        Box(
                            modifier = Modifier
                                .size(64.dp, 32.dp)
                                .scale(animatedBackgroundScale(isSelected))
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.tertiaryContainer)
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Icon(
                                modifier = Modifier.scale(iconScale(isSelected = isSelected)),
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title,
                                tint = color
                            )
                        }
                    }
                    if (isShowLabel) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            modifier = Modifier.scale(textScale(isSelected = isSelected)),
                            text = item.title,
                            style = MaterialTheme.typography.labelSmall.copy(
                                lineHeight = 14.sp,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                color = color
                            ),
                            fontFamily = if (isSelected) FontNunito.bold() else FontNunito.medium()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomNavigationRail(
    items: List<BottomNavigationItem>,
    selectedTab: BottomBarConfiguration,
    isShowLabel: Boolean,
    onClickTab: (BottomBarConfiguration) -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    Surface(
        color = MaterialTheme.colorScheme.primary,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(100.dp)
                .windowInsetsPadding(NavigationRailDefaults.windowInsets)
                .selectableGroup()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items.forEach { item ->
                val isSelected = selectedTab == item.bottomBarConfiguration
                val selectedColor = MaterialTheme.colorScheme.tertiary
                val unselectedColor = Color.Gray.copy(alpha = 0.8F)
                val color = if (isSelected) selectedColor else unselectedColor
                val interactionSource = remember { MutableInteractionSource() }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = isSelected,
                            onClick = {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                onClickTab(item.bottomBarConfiguration)
                            },
                            role = Role.Tab,
                            interactionSource = interactionSource,
                            indication = null,
                        )
                ) {
                    // Animated background that expands from center
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .scale(animatedBackgroundScale(isSelected))
                            .clip(animatedClip(isSelected))
                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .scale(iconScale(isSelected = isSelected)),
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title,
                            tint = color
                        )
                        if (isShowLabel) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                modifier = Modifier.scale(textScale(isSelected = isSelected)),
                                text = item.title,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 12.sp,
                                    lineHeight = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                    color = color
                                ),
                                fontFamily = if (isSelected) FontNunito.bold() else FontNunito.medium()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun iconScale(isSelected: Boolean): Float {
    val value by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1.0f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = 300f
        ),
        label = "iconScale"
    )
    return value
}

@Composable
private fun textScale(isSelected: Boolean): Float {
    val value by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1.0f,
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = 400f
        ),
        label = "textScale"
    )
    return value
}

@Composable
private fun animatedBackgroundScale(isSelected: Boolean): Float {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.0f else 0.0f,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = 0
        ),
        label = "backgroundScale"
    )
    return scale
}

@Composable
private fun animatedClip(isSelected: Boolean): RoundedCornerShape {
    val cornerRadius by animateFloatAsState(
        targetValue = if (isSelected) 24f else 0f,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = 0
        ),
        label = "cornerRadius"
    )
    return remember(cornerRadius) {
        RoundedCornerShape(cornerRadius.dp)
    }
}