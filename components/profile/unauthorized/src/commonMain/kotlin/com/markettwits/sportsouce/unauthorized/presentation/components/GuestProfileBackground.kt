package com.markettwits.sportsouce.unauthorized.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.markettwits.core_ui.items.theme.DarkSauceColors
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import com.markettwits.core_ui.items.image.AlbumImage
import com.markettwits.core_ui.items.image.AlbumImageSize
import com.markettwits.sportsouce.unauthorized.domain.GuestWelcomeContent

/** No bundled bitmap: cached CDN photos reveal over a lightweight design-system backdrop. */
@Composable
internal fun GuestProfileBackground(
    content: GuestWelcomeContent,
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader = SingletonImageLoader.get(LocalPlatformContext.current),
) {
    Box(modifier.drawWithCache {
        val backdrop = Brush.radialGradient(
            colors = listOf(DarkSauceColors.actionSoft, DarkSauceColors.background),
            center = Offset(size.width * .75f, size.height * .3f),
            radius = size.maxDimension,
        )
        onDrawBehind { drawRect(backdrop) }
    }) {
        val url = content.imageUrl
        if (!url.isNullOrBlank()) {
            var hasRemotePhoto by remember(url) { mutableStateOf(false) }
            val reveal = remember(url) { Animatable(0f) }
            LaunchedEffect(hasRemotePhoto) {
                if (hasRemotePhoto) reveal.animateTo(1f, tween(550))
            }
            AlbumImage(
                url = url,
                size = AlbumImageSize.Full,
                modifier = Modifier.fillMaxSize().graphicsLayer { alpha = reveal.value },
                alignment = BiasAlignment(content.imageAlignmentX, content.imageAlignmentY),
                onPainter = { hasRemotePhoto = true },
                // Cached photos load from Coil disk storage before trying the network.
                loading = {},
                error = { _, _ -> },
                imageLoader = imageLoader,
            )
        }
    }
}
