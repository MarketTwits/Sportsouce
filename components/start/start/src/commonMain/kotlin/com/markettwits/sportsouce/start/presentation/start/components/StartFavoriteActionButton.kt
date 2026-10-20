package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import com.markettwits.core_ui.items.theme.SauceTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.disabled

@Composable
internal fun StartFavoriteActionButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    isFavorite: Boolean,
    onClick: () -> Unit,
) {
    SmallFloatingActionButton(
        modifier = modifier
            .semantics {
                contentDescription = if (isFavorite) "Убрать старт из избранного" else "Добавить старт в избранное"
                stateDescription = if (isLoading) "Сохраняется" else if (isFavorite) "В избранном" else "Не в избранном"
                if (isLoading) disabled()
            }
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .padding(10.dp),
        containerColor = SauceTheme.colors.surfaceRaised,
        contentColor = SauceTheme.colors.textPrimary,
        onClick = { if (!isLoading) onClick() },
    ) {

        AnimatedContent(
            targetState = isLoading,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            label = "favorite_loading_anim"
        ) { loading ->
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = SauceTheme.colors.action,
                    trackColor = SauceTheme.colors.border,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    modifier = Modifier.size(20.dp),
                    tint = if (isFavorite) SauceTheme.colors.danger.content else SauceTheme.colors.textPrimary,
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null
                )
            }
        }
    }
}
