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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.markettwits.core_ui.items.theme.SportSouceColor

@Composable
internal fun StartFavoriteActionButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    isFavorite: Boolean,
    onClick: () -> Unit,
) {
    SmallFloatingActionButton(
        modifier = modifier
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .padding(10.dp),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.tertiary,
        onClick = onClick,
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
                    trackColor = MaterialTheme.colorScheme.tertiary,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    modifier = Modifier.size(20.dp),
                    tint = if (isFavorite) SportSouceColor.SportSouceStartEndedPink else MaterialTheme.colorScheme.tertiary,
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Add Start to Favorite"
                )
            }
        }
    }
}
