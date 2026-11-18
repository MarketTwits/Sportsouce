package com.markettwits.sportsouce.bottom_bar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.components.navigation.sauceVerticalScroll
import com.markettwits.core_ui.items.theme.SauceShapes
import com.markettwits.core_ui.items.theme.SauceSpacing
import com.markettwits.core_ui.items.theme.SauceTheme
import com.markettwits.sportsouce.bottom_bar.model.BottomBarConfiguration
import com.markettwits.sportsouce.bottom_bar.model.BottomNavigationItem

@Composable
internal fun WideScreenNavigation(
    items: List<BottomNavigationItem>,
    selectedTab: BottomBarConfiguration,
    showLabels: Boolean,
    profilePhotoUrl: String?,
    onSelect: (BottomBarConfiguration) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = SauceTheme.colors
    val itemShape = SauceShapes.field
    Row(modifier.background(colors.surface)) {
        Column(
            Modifier.weight(1f).fillMaxHeight()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical + WindowInsetsSides.Start))
                .sauceVerticalScroll(rememberScrollState())
                .selectableGroup()
                .padding(horizontal = SauceSpacing.small, vertical = SauceSpacing.large),
            verticalArrangement = Arrangement.spacedBy(SauceSpacing.small),
        ) {
            items.forEach { item ->
                val selected = item.bottomBarConfiguration == selectedTab
                val foreground = if (selected) colors.action else colors.textSecondary
                Box(
                    Modifier.fillMaxWidth().heightIn(min = if (showLabels) 84.dp else 68.dp)
                        .navigationTab(item, selected, itemShape) { onSelect(item.bottomBarConfiguration) },
                    contentAlignment = Alignment.Center,
                ) {
                    if (selected) {
                        Box(
                            Modifier.align(Alignment.CenterStart).padding(start = SauceSpacing.extraSmall)
                                .size(width = 3.dp, height = 32.dp)
                                .background(colors.action, SauceShapes.button),
                        )
                    }
                    Column(
                        Modifier.fillMaxWidth().padding(horizontal = SauceSpacing.extraSmall, vertical = SauceSpacing.small),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(SauceSpacing.small),
                    ) {
                        CompositionLocalProvider(LocalContentColor provides foreground) {
                            NavigationIcon(item, selected, showLabels, profilePhotoUrl, size = 28.dp)
                            if (showLabels) {
                                Text(
                                    item.title,
                                    modifier = Modifier.fillMaxWidth(),
                                    style = MaterialTheme.typography.labelLarge,
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
        VerticalDivider(Modifier.fillMaxHeight(), color = colors.border)
    }
}
