package com.markettwits.sportsouce.start.presentation.start.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import com.markettwits.core_ui.items.theme.SauceTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.disabled
import com.markettwits.core_ui.items.components.buttons.SauceFloatingActionButton
import com.markettwits.core_ui.items.components.buttons.SauceFloatingActionGlass

@Composable
internal fun StartFavoriteActionButton(
    glass: SauceFloatingActionGlass?,
    isLoading: Boolean,
    isFavorite: Boolean,
    onClick: () -> Unit,
) {
    SauceFloatingActionButton(
        modifier = Modifier.semantics {
            contentDescription = if (isFavorite) "Убрать старт из избранного" else "Добавить старт в избранное"
            stateDescription = if (isLoading) "Сохраняется" else if (isFavorite) "В избранном" else "Не в избранном"
            if (isLoading) disabled()
        },
        glass = glass,
        onClick = { if (!isLoading) onClick() },
    ) {

        AnimatedContent(
            targetState = isLoading to isFavorite,
            transitionSpec = {
                (fadeIn() + scaleIn(initialScale = .82f)) togetherWith (fadeOut() + scaleOut(targetScale = .88f))
            },
            label = "favorite_loading_anim"
        ) { (loading, favorite) ->
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
                    tint = if (favorite) SauceTheme.colors.danger.content else SauceTheme.colors.textPrimary,
                    imageVector = if (favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null
                )
            }
        }
    }
}
