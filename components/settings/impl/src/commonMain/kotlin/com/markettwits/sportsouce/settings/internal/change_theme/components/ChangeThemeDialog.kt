package com.markettwits.sportsouce.settings.internal.change_theme.components

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.markettwits.core_ui.items.components.buttons.ButtonContentBase
import com.markettwits.core_ui.items.components.cards.OnBackgroundCard
import com.markettwits.core_ui.items.theme.FontNunito
import com.markettwits.core_ui.items.theme.Shapes

@Composable
fun ChangeThemeDialog(
    onDismissRequest: () -> Unit,
    items: List<ColorThemeUi>,
    onClick: (ColorThemeUi) -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        AnimatedVisibility(
            visible = true,
            enter = scaleIn(
                animationSpec = spring(
                    dampingRatio = 0.8f,
                    stiffness = 300f
                )
            ) + fadeIn(animationSpec = tween(300)),
            exit = scaleOut(
                animationSpec = tween(200)
            ) + fadeOut(animationSpec = tween(200))
        ) {
            ChangeThemeContent(
                items = items,
                title = "Выбор темы",
                dismiss = onDismissRequest,
                selectedItem = {
                    onClick(it)
                }
            )
        }
    }
}

@Composable
private fun ChangeThemeContent(
    modifier: Modifier = Modifier,
    items: List<ColorThemeUi>,
    title: String,
    selectedItem: (ColorThemeUi) -> Unit,
    dismiss: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = Shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 24.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with icon and title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = FontNunito.bold(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Theme options
            FilterBody(items = items, selectedItem = selectedItem::invoke)

            Spacer(modifier = Modifier.height(8.dp))

            // Cancel button
            ButtonContentBase(
                modifier = Modifier.fillMaxWidth(),
                title = "Отмена",
                onClick = { dismiss() },
                borderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            )
        }
    }
}

@Composable
fun FilterBody(
    modifier: Modifier = Modifier,
    items: List<ColorThemeUi>,
    selectedItem: (ColorThemeUi) -> Unit,
) = LazyColumn(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
        itemsIndexed(items) { index, item ->
            FilterPosition(
                modifier = Modifier.padding(4.dp),
                title = item.title,
                selected = item.checked
            ) {
                selectedItem(item)
            }
        }
    }

@Composable
fun FilterPosition(
    modifier: Modifier = Modifier,
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected)
            MaterialTheme.colorScheme.outlineVariant
        else
            MaterialTheme.colorScheme.background,
        animationSpec = tween(200),
        label = "backgroundColor"
    )

    OnBackgroundCard(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = Shapes.medium,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Theme icon
            val themeIcon = getThemeIcon(title)
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        if (selected)
                            MaterialTheme.colorScheme.secondary
                        else
                            MaterialTheme.colorScheme.background,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = themeIcon,
                    contentDescription = null,
                    tint = if (selected)
                        MaterialTheme.colorScheme.onSecondary
                    else
                        MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Theme title
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = FontNunito.medium(),
                    fontSize = 16.sp,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                )
            }

            // Radio button
            RadioButton(
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.secondary,
                    unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                selected = selected,
                onClick = { onClick() }
            )
        }
    }
}

@Composable
private fun getThemeIcon(title: String): ImageVector {
    return when {
        title.contains("Светл", ignoreCase = true) || title.contains(
            "Light",
            ignoreCase = true
        ) -> Icons.Default.LightMode

        title.contains("Темн", ignoreCase = true) || title.contains("Dark", ignoreCase = true) -> Icons.Default.DarkMode
        else -> Icons.Default.Settings // System/Auto theme
    }
}